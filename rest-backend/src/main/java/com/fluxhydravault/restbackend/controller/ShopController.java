package com.fluxhydravault.restbackend.controller;

import com.fluxhydravault.restbackend.NotFoundException;
import com.fluxhydravault.restbackend.services.ItemService;
import com.fluxhydravault.restbackend.services.ShopItemService;
import com.fluxhydravault.restbackend.services.TokenService;
import com.fluxhydravault.restbackend.model.ShopItem;
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
@RequestMapping("/shop")
public class ShopController {
    private TokenService tokenService;
    private ItemService itemService;
    private ShopItemService shopItemService;

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Autowired
    public void setItemService(ItemService itemService) {
        this.itemService = itemService;
    }

    @Autowired
    public void setShopItemService(ShopItemService shopItemService) {
        this.shopItemService = shopItemService;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Map<String, Object> newShopItem(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @RequestParam("item_id") String itemID,
            @RequestParam("gold_cost") int gold,
            @RequestParam("diamond_cost") int diamond,
            @RequestParam("credit_cost") int credit,
            @RequestParam("discount") float discount
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenService);

        if (itemService.getItem(itemID) == null) {
            throw new NotFoundException("Item@" + itemID);
        }

        ShopItem result = shopItemService.newShopItem(itemID, gold, diamond, credit, discount);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("response", "201 Created");
        map.put("message", "ShopItem Successfully Added");
        map.put("data", result);

        return map;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/{id}", method = { RequestMethod.PUT, RequestMethod.PATCH })
    public Map<String, Object> updateShopItem(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String shopID,
            @RequestParam(name = "item_id", required = false) String itemID,
            @RequestParam(name = "gold_cost", required = false) Integer gold,
            @RequestParam(name = "diamond_cost", required = false) Integer diamond,
            @RequestParam(name = "credit_cost", required = false) Integer credit,
            @RequestParam(name = "discount", required = false) Float discount
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenService);

        if (shopItemService.getShopItem(shopID) == null) {
            throw new NotFoundException("ShopItem@" + shopID);
        }

        if (itemID != null) {
            if (itemService.getItem(itemID) == null) {
                throw new NotFoundException("Item@" + itemID);
            }
            shopItemService.changeItemID(shopID, itemID);
        }
        if (gold != null) {
            shopItemService.changeGoldCost(shopID, gold);
        }
        if (diamond != null) {
            shopItemService.changeDiamondCost(shopID, diamond);
        }
        if (credit != null) {
            shopItemService.changeCreditCost(shopID, credit);
        }
        if (discount != null) {
            shopItemService.changeDiscount(shopID, discount);
        }

        ShopItem result = shopItemService.getShopItem(shopID);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("message", "Update success.");
        map.put("data", result);
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ShopItem> listShopItems(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @RequestParam(name = "start", required = false) Integer start,
            @RequestParam(name = "n", required = false) Integer limit,
            HttpServletResponse response
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "BOTH", tokenService);

        response.addHeader("X-Total-Count", shopItemService.getNumberOfShopItems().toString());
        if (start == null && limit == null) {
            return shopItemService.getShopItems(0, 10);
        }
        else if (start == null) {
            return shopItemService.getShopItems(0, limit);
        }
        else if (limit == null) {
            return shopItemService.getShopItems(start, 10);
        }
        else {
            return shopItemService.getShopItems(start, limit);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Map<String, Object> searchShopItem(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @RequestParam(value = "q", required = false) String itemName
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "BOTH", tokenService);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("matched_result", shopItemService.getShopItemByItemName(itemName));
        map.put("possible_results", shopItemService.searchShopItemByItemName(itemName));
        return map;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteShopItem(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @PathVariable("id") String shopItemID
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenService);

        shopItemService.deleteShopItem(shopItemID);
    }
}
