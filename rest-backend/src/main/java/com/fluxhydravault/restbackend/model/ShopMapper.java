package com.fluxhydravault.restbackend.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ShopMapper implements RowMapper<Shop> {
    @Override
    public Shop mapRow(ResultSet resultSet, int i) throws SQLException {
        Shop shop = new Shop();
        shop.setShop_item_id(resultSet.getString(1));
        shop.setItem_id(resultSet.getString(2));
        shop.setDiamond_cost(resultSet.getInt(3));
        shop.setGold_cost(resultSet.getInt(4));
        shop.setCredit_cost(resultSet.getInt(5));
        shop.setDiscount(resultSet.getFloat(6));

        return shop;
    }
}
