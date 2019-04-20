package com.fluxhydravault.restbackend.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemMapper implements RowMapper<Item> {
    @Override
    public Item mapRow(ResultSet resultSet, int i) throws SQLException {
        Item item = new Item();
        item.setItem_id(resultSet.getString(1));
        item.setItem_category(resultSet.getByte(2));
        item.setItem_name(resultSet.getString(3));
        item.setDescription(resultSet.getString(4));
        item.setModel_location(resultSet.getString(5));

        return item;
    }
}
