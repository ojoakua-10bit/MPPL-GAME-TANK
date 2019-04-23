package com.fluxhydravault.restbackend.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ShopItemMapper implements RowMapper<ShopItem> {
    @Override
    public ShopItem mapRow(ResultSet resultSet, int i) throws SQLException {
        ShopItem shopItem = new ShopItem();
        shopItem.setShop_item_id(resultSet.getString(1));
        shopItem.setItem_id(resultSet.getString(2));
        shopItem.setDiamond_cost(resultSet.getInt(3));
        shopItem.setGold_cost(resultSet.getInt(4));
        shopItem.setCredit_cost(resultSet.getInt(5));
        shopItem.setDiscount(resultSet.getFloat(6));

        return shopItem;
    }
}
