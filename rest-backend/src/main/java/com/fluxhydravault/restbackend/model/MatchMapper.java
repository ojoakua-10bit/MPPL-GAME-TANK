package com.fluxhydravault.restbackend.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MatchMapper implements RowMapper<Match> {
    @Override
    public Match mapRow(ResultSet resultSet, int i) throws SQLException {
        Match match = new Match();
        match.setMatch_id(resultSet.getString(1));
        match.setPlayer_id(resultSet.getString(2));
        match.setMatch_status(resultSet.getByte(3));
        match.setScore(resultSet.getInt(4));
        match.setTotal_damage(resultSet.getInt(5));
        match.setGold_gained(resultSet.getInt(6));
        match.setItem_gained(resultSet.getString(7));

        return match;
    }
}
