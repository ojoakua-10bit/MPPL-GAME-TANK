package com.fluxhydravault.restbackend.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerMapper implements RowMapper<Player> {
    @Override
    public Player mapRow(ResultSet resultSet, int i) throws SQLException {
        Player player = new Player();
        player.setPlayer_id(resultSet.getString(1));
        player.setUsername(resultSet.getString(2));
        player.setPlayer_name(resultSet.getString(4));
        player.setRank(resultSet.getInt(5));
        player.setXp(resultSet.getInt(6));
        player.setDiamond_count(resultSet.getInt(7));
        player.setGold_count(resultSet.getInt(8));
        player.setCredit_balance(resultSet.getInt(9));
        player.setInventory(resultSet.getInt(10));
        player.setAvatar(resultSet.getString(11));
        player.setBan_status(resultSet.getByte(12) == 1);

        return player;
    }
}
