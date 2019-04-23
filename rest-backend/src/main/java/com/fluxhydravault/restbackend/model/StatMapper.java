package com.fluxhydravault.restbackend.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatMapper implements RowMapper<Stat> {
    @Override
    public Stat mapRow(ResultSet resultSet, int i) throws SQLException {
        Stat stat = new Stat();
        stat.setStat_id(resultSet.getLong(1));
        stat.setType(StatType.valueOf(resultSet.getString(2)));
        stat.setName(resultSet.getString(3));
        stat.setValue(resultSet.getDouble(4));

        return stat;
    }
}
