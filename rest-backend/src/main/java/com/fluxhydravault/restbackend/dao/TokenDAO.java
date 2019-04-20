package com.fluxhydravault.restbackend.dao;

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
public class TokenDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    public String generateToken(String playerID) {
        Random random = new Random();
        String token;
        do {
            token = Digestive.sha256(Long.toString(random.nextLong()));
        } while (getToken(token) != null);
        Token oldToken = getValidToken(playerID);
        if (oldToken != null) {
            deactivateToken(oldToken.getToken());
        }

        String SQL = "INSERT INTO token VALUES (?, ?, ?)";
        jdbcTemplateObject.update(SQL, token, playerID, 1);
        return token;
    }

    public Token getToken(String token) {
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

    public boolean isValidToken(String token) {
        Token tmp = getToken(token);

        if (tmp == null) return false;
        else return (tmp.getStatus() == 1);
    }

    private Token getValidToken(String playerID) {
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

    private void deactivateToken(String token) {
        String SQL = "UPDATE token SET status='0' WHERE token=?";
        jdbcTemplateObject.update(SQL, token);
    }
}
