package com.fluxhydravault.restbackend;

import com.fluxhydravault.restbackend.dao.ItemDAO;
import com.fluxhydravault.restbackend.dao.ShopItemDAO;
import com.fluxhydravault.restbackend.dao.TokenDAO;
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
    private TokenDAO tokenDAO;
    private ItemDAO itemDAO;
    private ShopItemDAO shopItemDAO;

    @Autowired
    public void setTokenDAO(TokenDAO tokenDAO) {
        this.tokenDAO = tokenDAO;
    }

    @Autowired
    public void setItemDAO(ItemDAO itemDAO) {
        this.itemDAO = itemDAO;
    }

    @Autowired
    public void setShopItemDAO(ShopItemDAO shopItemDAO) {
        this.shopItemDAO = shopItemDAO;
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
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenDAO);

        if (itemDAO.getItem(itemID) == null) {
            throw new NotFoundException("Item@" + itemID);
        }

        ShopItem result = shopItemDAO.newShopItem(itemID, gold, diamond, credit, discount);
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
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenDAO);

        if (shopItemDAO.getShopItem(shopID) == null) {
            throw new NotFoundException("ShopItem@" + shopID);
        }

        if (itemID != null) {
            if (itemDAO.getItem(itemID) == null) {
                throw new NotFoundException("Item@" + itemID);
            }
            shopItemDAO.changeItemID(shopID, itemID);
        }
        if (gold != null) {
            shopItemDAO.changeGoldCost(shopID, gold);
        }
        if (diamond != null) {
            shopItemDAO.changeDiamondCost(shopID, diamond);
        }
        if (credit != null) {
            shopItemDAO.changeCreditCost(shopID, credit);
        }
        if (discount != null) {
            shopItemDAO.changeDiscount(shopID, discount);
        }

        ShopItem result = shopItemDAO.getShopItem(shopID);
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
        HeaderChecker.checkHeader(appToken, userToken, "BOTH", tokenDAO);

        response.addHeader("X-Total-Count", shopItemDAO.getNumberOfShopItems().toString());
        if (start == null && limit == null) {
            return shopItemDAO.getShopItems(0, 10);
        }
        else if (start == null) {
            return shopItemDAO.getShopItems(0, limit);
        }
        else if (limit == null) {
            return shopItemDAO.getShopItems(start, 10);
        }
        else {
            return shopItemDAO.getShopItems(start, limit);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Map<String, Object> searchShopItem(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @RequestParam(value = "q", required = false) String itemName
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "BOTH", tokenDAO);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("matched_result", shopItemDAO.getShopItemByItemName(itemName));
        map.put("possible_results", shopItemDAO.searchShopItemByItemName(itemName));
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
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenDAO);

        shopItemDAO.deleteShopItem(shopItemID);
    }
}
