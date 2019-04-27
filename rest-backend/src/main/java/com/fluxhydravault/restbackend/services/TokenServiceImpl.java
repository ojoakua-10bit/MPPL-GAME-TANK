package com.fluxhydravault.restbackend.services;

import com.fluxhydravault.restbackend.model.Token;
import com.fluxhydravault.restbackend.model.TokenMapper;
import com.fluxhydravault.restbackend.utils.Digestive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Random;

@Service
public class TokenServiceImpl implements TokenService {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    @Override
    public String generateUserToken(String playerID) {
        Random random = new Random();
        String token;
        do {
            token = Digestive.sha256(Long.toString(random.nextLong()));
        } while (getUserToken(token) != null);
        Token oldToken = getValidUserToken(playerID);
        if (oldToken != null) {
            deactivateUserToken(oldToken.getToken());
        }

        String SQL = "INSERT INTO token VALUES (?, ?, ?)";
        jdbcTemplateObject.update(SQL, token, playerID, 1);
        return token;
    }

    @Override
    public Token getUserToken(String token) {
        Token tmp;
        try {
            tmp = jdbcTemplateObject
                    .queryForObject("SELECT * FROM token WHERE token=?",
                            new Object[]{token}, new TokenMapper());
        } catch (IncorrectResultSizeDataAccessException e) {
            tmp = null;
        }
        return tmp;
    }

    @Override
    public boolean isValidUserToken(String token) {
        Token tmp = getUserToken(token);

        if (tmp == null) return false;
        else return (tmp.getStatus() == 1);
    }

    @Override
    public String generateAdminToken(String adminID) {
        Random random = new Random();
        String token;
        do {
            token = Digestive.sha256(Long.toString(random.nextLong()));
        } while (getAdminToken(token) != null);
        Token oldToken = getValidAdminToken(adminID);
        if (oldToken != null) {
            deactivateAdminToken(oldToken.getToken());
        }

        String SQL = "INSERT INTO admin_token VALUES (?, ?, ?)";
        jdbcTemplateObject.update(SQL, token, adminID, 1);
        return token;
    }

    @Override
    public Token getAdminToken(String token) {
        Token tmp;
        try {
            tmp = jdbcTemplateObject
                    .queryForObject("SELECT * FROM admin_token WHERE token=?",
                            new Object[]{token}, new TokenMapper());
        } catch (IncorrectResultSizeDataAccessException e) {
            tmp = null;
        }
        return tmp;
    }

    @Override
    public boolean isValidAdminToken(String token) {
        Token tmp = getAdminToken(token);

        if (tmp == null) return false;
        else return (tmp.getStatus() == 1);
    }

    private Token getValidUserToken(String playerID) {
        Token token;
        try {
            token = jdbcTemplateObject
                    .queryForObject("SELECT * FROM token WHERE player_id=? AND status=1",
                            new Object[]{playerID}, new TokenMapper());
        } catch (IncorrectResultSizeDataAccessException e) {
            token = null;
        }
        return token;
    }

    private Token getValidAdminToken(String adminID) {
        Token token;
        try {
            token = jdbcTemplateObject
                    .queryForObject("SELECT * FROM admin_token WHERE admin_id=? AND status=1",
                            new Object[]{adminID}, new TokenMapper());
        } catch (IncorrectResultSizeDataAccessException e) {
            token = null;
        }
        return token;
    }

    private void deactivateUserToken(String token) {
        String SQL = "UPDATE token SET status='0' WHERE token=?";
        jdbcTemplateObject.update(SQL, token);
    }

    private void deactivateAdminToken(String token) {
        String SQL = "UPDATE admin_token SET status='0' WHERE token=?";
        jdbcTemplateObject.update(SQL, token);
    }
}
