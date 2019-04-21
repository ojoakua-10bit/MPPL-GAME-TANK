package com.fluxhydravault.restbackend.dao;

import com.fluxhydravault.restbackend.model.Match;
import com.fluxhydravault.restbackend.model.MatchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class MatchDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    public Match newMatch(String playerID, boolean matchStatus, int score, int totalDamage, int goldGained, String itemGained) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String matchID = playerID + "-00" + format.format(new Date());

        if (itemGained == null) {
            jdbcTemplateObject
                    .update("INSERT INTO match_result(match_id, player_id, match_status, score, total_damage, gold_gained) " +
                            "VALUES (?, ?, ?, ?, ?, ?)", matchID, playerID, matchStatus ? 1 : 0, score, totalDamage, goldGained);
        }
        else {
            jdbcTemplateObject.update("INSERT INTO match_result VALUES (?, ?, ?, ?, ?, ?, ?)",
                    matchID, playerID, matchStatus ? 1 : 0, score, totalDamage, goldGained, itemGained);
        }

        return getMatch(matchID);
    }

    public Match getMatch(String matchID) {
        Match temp;
        try {
            temp = jdbcTemplateObject.queryForObject("SELECT * FROM match_result WHERE match_id=?",
                    new Object[]{matchID}, new MatchMapper());
        } catch (IncorrectResultSetColumnCountException e) {
            temp = null;
        }
        return temp;
    }

    public List<Match> getUserMatches(String playerID) {
        return jdbcTemplateObject.query("SELECT * FROM match_result WHERE player_id=? LIMIT 300",
                new Object[]{playerID}, new MatchMapper());
    }

    public List<Match> getUserMatches(String playerID, int start, int limit) {
        return jdbcTemplateObject.query("SELECT * FROM match_result WHERE player_id=? LIMIT ?, ?",
                new Object[]{playerID, start, limit}, new MatchMapper());
    }

    public Integer getUserMatchesCount(String playerID) {
        return jdbcTemplateObject.queryForObject("SELECT COUNT(*) from match_result WHERE player_id=?",
                new Object[]{playerID}, (resultSet, i) -> resultSet.getInt(1));
    }
}
