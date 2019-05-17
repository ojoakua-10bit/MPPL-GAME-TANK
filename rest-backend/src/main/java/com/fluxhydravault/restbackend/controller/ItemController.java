package com.fluxhydravault.restbackend.controller;

import com.fluxhydravault.restbackend.InputFormatException;
import com.fluxhydravault.restbackend.NotFoundException;
import com.fluxhydravault.restbackend.services.ItemService;
import com.fluxhydravault.restbackend.services.StatService;
import com.fluxhydravault.restbackend.services.TokenService;
import com.fluxhydravault.restbackend.model.Item;
import com.fluxhydravault.restbackend.model.ItemCategory;
import com.fluxhydravault.restbackend.model.Stat;
import com.fluxhydravault.restbackend.utils.HeaderChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/items")
public class ItemController {
    private TokenService tokenService;
    private ItemService itemService;
    private StatService statService;

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Autowired
    public void setItemService(ItemService itemService) {
        this.itemService = itemService;
    }

    @Autowired
    public void setStatService(StatService statService) {
        this.statService = statService;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Map<String, Object> newPlayer(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @RequestParam("item_category") String categoryString,
            @RequestParam("item_name") String itemName,
            @RequestParam("description") String description,
            @RequestParam(value = "location", required = false) String location
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenService);

        ItemCategory category;
        try {
            category = ItemCategory.valueOf(categoryString);
        } catch (IllegalArgumentException e) {
            throw new InputFormatException("Unknown item_category: " + categoryString);
        }

        Item result;
        if (location.equals("null") || location.isEmpty()) {
            result = itemService.newItem(category, itemName, description, null);
        } else {
            result = itemService.newItem(category, itemName, description, location);
        }
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("response", "201 Created");
        map.put("message", "Item Successfully Added");
        map.put("data", result);

        return map;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/{id}", method = { RequestMethod.PATCH, RequestMethod.PUT })
    public Map<String, Object> updateItem(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @RequestParam Map<String, String> params,
            @PathVariable("id") String itemID
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenService);

        if (itemService.getItem(itemID) == null) {
            throw new NotFoundException("Item@" + itemID);
        }

        try {
            if (params.containsKey("item_category")) {
                itemService.changeItemCategory(itemID, ItemCategory.valueOf(params.get("item_category")));
            }
        } catch (IllegalArgumentException e) {
            throw new InputFormatException("Unknown item_category: " + params.get("item_category"));
        }
        if (params.containsKey("item_name")) {
            itemService.changeItemName(itemID, params.get("item_name"));
        }
        if (params.containsKey("description")) {
            itemService.changeItemDescription(itemID, params.get("description"));
        }
        if (params.containsKey("model_location")) {
            String tmp = params.get("model_location");
            if (tmp == null || tmp.equals("null")) {
                itemService.changeItemModelLocation(itemID, null);
            }
            else {
                itemService.changeItemModelLocation(itemID, tmp);
            }
        }

        Item result = itemService.getItem(itemID);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("response", "201 Created");
        map.put("message", "Item update success.");
        map.put("data", result);
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Item> listItems(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @RequestParam(name = "start", required = false) Integer start,
            @RequestParam(name = "n", required = false) Integer limit,
            HttpServletResponse response
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenService);

        response.addHeader("X-Total-Count", itemService.getNumberOfItems().toString());
        if (start == null && limit == null) {
            return itemService.getItems(0, 10);
        }
        else if (start == null) {
            return itemService.getItems(0, limit);
        }
        else if (limit == null) {
            return itemService.getItems(start, 10);
        }
        else {
            return itemService.getItems(start, limit);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Map<String, Object> searchItem(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @RequestParam(value = "q", required = false) String itemName
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenService);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("matched_result", itemService.getItemByName(itemName));
        map.put("possible_results", itemService.searchItemByName(itemName));
        return map;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteItem(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String itemID
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenService);

        itemService.deleteItem(itemID);
    }

    @ResponseBody
    @RequestMapping(value = "/{id}/stats", method = RequestMethod.GET)
    public List<Stat> getItemStats(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String itemID
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "BOTH", tokenService);

        return statService.getItemStats(itemID);
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/{id}/stats", method = RequestMethod.POST)
    public List<Stat> addItemStats(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String itemID,
            @RequestParam(name = "stat_id", required = false) Long statID,
            @RequestParam(name = "stat_name", required = false) String statName
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenService);

        if (statID != null && statName != null) {
            throw new InputFormatException();
        }
        else if (statID != null) {
            statService.addStatToItem(itemID, statID);
        }
        else if (statName != null) {
            statService.addStatToItem(itemID, statName);
        }
        else {
            throw new InputFormatException();
        }

        return statService.getItemStats(itemID);
    }

    @ResponseBody
    @RequestMapping(value = "/{id}/stats", method = RequestMethod.DELETE)
    public List<Stat> deleteItemStats(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String itemID,
            @RequestParam(name = "stat_id", required = false) Long statID,
            @RequestParam(name = "stat_name", required = false) String statName
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenService);

        if (statID != null && statName != null) {
            throw new InputFormatException();
        }
        else if (statID != null) {
            statService.deleteStatFromItem(itemID, statID);
        }
        else if (statName != null) {
            statService.deleteStatFromItem(itemID, statName);
        }
        else {
            throw new InputFormatException();
        }

        return statService.getItemStats(itemID);
    }
}
