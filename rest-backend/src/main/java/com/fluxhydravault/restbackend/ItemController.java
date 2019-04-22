package com.fluxhydravault.restbackend;

import com.fluxhydravault.restbackend.dao.ItemDAO;
import com.fluxhydravault.restbackend.dao.TokenDAO;
import com.fluxhydravault.restbackend.model.Item;
import com.fluxhydravault.restbackend.model.ItemCategory;
import com.fluxhydravault.restbackend.utils.HeaderChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/items")
public class ItemController {
    private TokenDAO tokenDAO;
    private ItemDAO itemDAO;

    @Autowired
    public void setTokenDAO(TokenDAO tokenDAO) {
        this.tokenDAO = tokenDAO;
    }

    @Autowired
    public void setItemDAO(ItemDAO itemDAO) {
        this.itemDAO = itemDAO;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Map<String, Object> newPlayer(
            @RequestHeader(name = "App-Token", required = false) String appToken,
            @RequestHeader(name = "User-Token", required = false) String userToken,
            @RequestParam("item_category") ItemCategory category,
            @RequestParam("item_name") String itemName,
            @RequestParam("description") String description,
            @RequestParam(value = "location", required = false) String location
    ) {
        HeaderChecker.checkHeader(appToken, userToken, "ADMIN", tokenDAO);

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

        try {
            if (params.containsKey("item_category")) {
                itemDAO.changeItemCategory(itemID, ItemCategory.valueOf(params.get("item_category")));
            }
        } catch (IllegalArgumentException e) {
            throw new InputFormatException("Unknown item_category.");
        }
        if (params.containsKey("item_name")) {
            itemDAO.changeItemName(itemID, params.get("item_name"));
        }
        if (params.containsKey("description")) {
            itemDAO.changeItemDescription(itemID, params.get("description"));
        }
        if (params.containsKey("model_location")) {
            itemDAO.changeItemModelLocation(itemID, params.get("model_location"));
        }

        Item result = itemDAO.getItem(itemID);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", new Date());
        map.put("message", "Update success.");
        map.put("data", result);
        return map;
    }
}
