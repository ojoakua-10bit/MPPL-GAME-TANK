package com.fluxhydravault.restbackend.dao;

import com.fluxhydravault.restbackend.NotFoundException;
import com.fluxhydravault.restbackend.model.ShopItem;
import com.fluxhydravault.restbackend.model.ShopItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ShopItemDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    public ShopItem newShopItem(String itemID, int gold, int diamond, int credit, float discount) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String shopID = format.format(new Date()) + '-' + itemID;

        jdbcTemplateObject.update("INSERT INTO shop_item VALUES (?, ?, ?, ?, ?, ?)",
                shopID, itemID, gold, diamond, credit, discount);

        return getShopItem(shopID);
    }

    public ShopItem getShopItem(String shopID) {
        ShopItem temp;
        try {
            temp = jdbcTemplateObject.queryForObject("SELECT * FROM shop_item WHERE shop_item_id=?",
                    new Object[]{ shopID }, new ShopItemMapper());
        } catch (IncorrectResultSetColumnCountException e) {
            temp = null;
        }
        return temp;
    }

    public ShopItem getShopItemByItemName(String itemName) {
        ShopItem temp;
        try {
            temp = jdbcTemplateObject.queryForObject("SELECT * FROM shop_item WHERE item_id IN " +
                            "(SELECT item.item_id FROM item WHERE item_name=?)",
                    new Object[]{ itemName }, new ShopItemMapper());
        } catch (IncorrectResultSetColumnCountException e) {
            temp = null;
        }
        return temp;
    }

    public List<ShopItem> searchShopItemByItemName(String itemName) {
        return jdbcTemplateObject.query("SELECT * FROM shop_item WHERE item_id IN " +
                        "(SELECT item.item_id FROM item WHERE item_name LIKE ?)",
                new Object[]{ '%' + itemName + '%' }, new ShopItemMapper());
    }

    public List<ShopItem> getShopItems(int start, int limit) {
        return jdbcTemplateObject.query("SELECT * FROM shop_item ORDER BY item_id LIMIT ?, ?",
                new ShopItemMapper(), start, limit);
    }

    public Integer getNumberOfShopItems() {
        return jdbcTemplateObject.queryForObject("SELECT COUNT(*) FROM shop_item",
                (resultSet, i) -> resultSet.getInt(1));
    }

    public void changeItemID(String shopID, String itemID) {
        String SQL = "UPDATE shop_item SET `item_id`=? WHERE `shop_item_id`=?";
        jdbcTemplateObject.update(SQL, itemID, shopID);
    }

    public void changeGoldCost(String shopID, int amount) {
        String SQL = "UPDATE shop_item SET `gold_cost`=? WHERE `shop_item_id`=?";
        jdbcTemplateObject.update(SQL, amount, shopID);
    }

    public void changeDiamondCost(String shopID, int amount) {
        String SQL = "UPDATE shop_item SET `diamond_cost`=? WHERE `shop_item_id`=?";
        jdbcTemplateObject.update(SQL, amount, shopID);
    }

    public void changeCreditCost(String shopID, int amount) {
        String SQL = "UPDATE shop_item SET `credit_cost`=? WHERE `shop_item_id`=?";
        jdbcTemplateObject.update(SQL, amount, shopID);
    }

    public void changeDiscount(String shopID, float amount) {
        String SQL = "UPDATE shop_item SET `discount`=? WHERE `shop_item_id`=?";
        jdbcTemplateObject.update(SQL, amount, shopID);
    }

    public void deleteShopItem(String shopID) {
        ShopItem temp = getShopItem(shopID);

        if (temp == null) {
            throw new NotFoundException("ShopItem@" + shopID);
        }

        String SQL = "DELETE FROM shop_item WHERE item_id=?";
        jdbcTemplateObject.update(SQL, shopID);
    }
}
