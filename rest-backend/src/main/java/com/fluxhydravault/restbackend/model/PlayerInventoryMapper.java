package com.fluxhydravault.restbackend.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerInventoryMapper implements RowMapper<PlayerInventory> {
    @Override
    public PlayerInventory mapRow(ResultSet rs, int rowNum) throws SQLException {
        PlayerInventory inventory = new PlayerInventory();
        inventory.setId(rs.getLong(1));
        Item item = new Item();
        item.setItem_id(rs.getString(2));
        item.setItem_category(ItemCategory.valueOf(rs.getString(3)));
        item.setItem_name(rs.getString(4));
        item.setDescription(rs.getString(5));
        item.setModel_location(rs.getString(6));
        inventory.setItem(item);
        return inventory;
    }
}
