package com.fluxhydravault.restbackend.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TokenMapper implements RowMapper<Token> {
    @Override
    public Token mapRow(ResultSet resultSet, int i) throws SQLException {
        Token token = new Token();
        token.setToken(resultSet.getString(1));
        token.setPlayer_id(resultSet.getString(2));
        token.setStatus(resultSet.getByte(3));

        return token;
    }
}
