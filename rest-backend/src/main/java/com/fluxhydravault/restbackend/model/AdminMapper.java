package com.fluxhydravault.restbackend.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminMapper implements RowMapper<Admin> {
    @Override
    public Admin mapRow(ResultSet rs, int rowNum) throws SQLException {
        Admin admin = new Admin();
        admin.setAdmin_id(rs.getString(1));
        admin.setUsername(rs.getString(2));
        admin.setAvatar(rs.getString(4));

        return admin;
    }
}
