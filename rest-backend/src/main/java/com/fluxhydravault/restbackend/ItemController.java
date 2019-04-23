package com.fluxhydravault.restbackend;

import com.fluxhydravault.restbackend.dao.ItemDAO;
import com.fluxhydravault.restbackend.dao.StatDAO;
import com.fluxhydravault.restbackend.dao.TokenDAO;
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
    private TokenDAO tokenDAO;
    private ItemDAO itemDAO;
    private StatDAO statDAO;

    @Autowired
    public void setTokenDAO(TokenDAO tokenDAO) {
        this.tokenDAO = tokenDAO;
    }

    @Autowired
    public void setItemDAO(ItemDAO itemDAO) {
        this.itemDAO = itemDAO;
    }

    @Autowired
    public void setStatDAO(StatDAO statDAO) {
        this.statDAO = statDAO;
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
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenDAO);

        ItemCategory category;
        try {
            category = ItemCategory.valueOf(categoryString);
        } catch (IllegalArgumentException e) {
            throw new InputFormatException("Unknown item_category: " + categoryString);
        }

        Item result = itemDAO.newItem(category, itemName, description, location);
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
    public Map<String, Object> updatePlayer(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @RequestParam Map<String, String> params,
            @PathVariable("id") String itemID
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenDAO);

        if (itemDAO.getItem(itemID) == null) {
            throw new NotFoundException("Item@" + itemID);
        }

        try {
            if (params.containsKey("item_category")) {
                itemDAO.changeItemCategory(itemID, ItemCategory.valueOf(params.get("item_category")));
            }
        } catch (IllegalArgumentException e) {
            throw new InputFormatException("Unknown item_category: " + params.get("item_category"));
        }
        if (params.containsKey("item_name")) {
            itemDAO.changeItemName(itemID, params.get("item_name"));
        }
        if (params.containsKey("description")) {
            itemDAO.changeItemDescription(itemID, params.get("description"));
        }
        if (params.containsKey("model_location")) {
            String tmp = params.get("model_location");
            if (tmp.equals("null")) {
                itemDAO.changeItemModelLocation(itemID, null);
            }
            else {
                itemDAO.changeItemModelLocation(itemID, tmp);
            }
        }

        Item result = itemDAO.getItem(itemID);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("message", "Update success.");
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
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenDAO);

        response.addHeader("X-Total-Count", itemDAO.getNumberOfItems().toString());
        if (start == null && limit == null) {
            return itemDAO.getItems(0, 10);
        }
        else if (start == null) {
            return itemDAO.getItems(0, limit);
        }
        else if (limit == null) {
            return itemDAO.getItems(start, 10);
        }
        else {
            return itemDAO.getItems(start, limit);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Map<String, Object> searchItem(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @RequestParam(value = "q", required = false) String itemName
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenDAO);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("matched_result", itemDAO.getItemByName(itemName));
        map.put("possible_results", itemDAO.searchItemByName(itemName));
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
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenDAO);

        itemDAO.deleteItem(itemID);
    }

    @ResponseBody
    @RequestMapping(value = "/{id}/stats", method = RequestMethod.GET)
    public List<Stat> getItemStats(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String itemID
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "BOTH", tokenDAO);

        return statDAO.getStats(itemID);
    }
}
