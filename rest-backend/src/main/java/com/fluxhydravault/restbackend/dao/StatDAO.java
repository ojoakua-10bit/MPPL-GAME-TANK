package com.fluxhydravault.restbackend.dao;

import com.fluxhydravault.restbackend.AlreadyExistsException;
import com.fluxhydravault.restbackend.NotFoundException;
import com.fluxhydravault.restbackend.model.Stat;
import com.fluxhydravault.restbackend.model.StatMapper;
import com.fluxhydravault.restbackend.model.StatType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
public class StatDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    public Stat newStat(StatType type, String name, double value) {
        if (getStatByName(name) != null) {
            throw new AlreadyExistsException("Stat@" + name);
        }
        jdbcTemplateObject.update("INSERT INTO stats_repo(`type`, `name`, `value`) VALUES (?, ?, ?)",
                type, name, value);

        return getStatByName(name);
    }

    public Stat getStatById(long id) {
        Stat temp;
        try {
            temp = jdbcTemplateObject.queryForObject("SELECT * FROM stats_repo WHERE stat_id=?",
                    new Object[]{ id }, new StatMapper());
        } catch (IncorrectResultSetColumnCountException e) {
            temp = null;
        }
        return temp;
    }

    public Stat getStatByName(String name) {
        Stat temp;
        try {
            temp = jdbcTemplateObject.queryForObject("SELECT * FROM stats_repo WHERE name=?",
                    new Object[]{ name }, new StatMapper());
        } catch (IncorrectResultSetColumnCountException e) {
            temp = null;
        }
        return temp;
    }

    public List<Stat> getAllStats() {
        return jdbcTemplateObject.query("SELECT * FROM stats_repo;", new StatMapper());
    }

    public List<Stat> searchStatByName(String name) {
        return jdbcTemplateObject.query("SELECT * FROM stats_repo WHERE name LIKE ?",
                new StatMapper(), "?" + name + "?");
    }

    public List<Stat> getStatsByType(StatType type) {
        return jdbcTemplateObject.query("SELECT * FROM stats_repo WHERE type=?",
                new StatMapper(), type.toString());
    }

    public void changeStatName(long statID, String name) {
        if (getStatByName(name) != null) {
            throw new AlreadyExistsException("Stat@" + name);
        }
        if (getStatById(statID) == null) {
            throw new NotFoundException("Stat@" + statID);
        }

        jdbcTemplateObject.update("UPDATE stats_repo SET `name`=? WHERE stat_id=?", name, statID);
    }

    public void changeStatName(String oldName, String newName) {
        if (getStatByName(newName) != null) {
            throw new AlreadyExistsException("Stat@" + newName);
        }
        if (getStatByName(oldName) == null) {
            throw new NotFoundException("Stat@" + oldName);
        }

        jdbcTemplateObject.update("UPDATE stats_repo SET `name`=? WHERE `name`=?", newName, oldName);
    }

    public void changeStatType(long statID, StatType type) {
        if (getStatById(statID) == null) {
            throw new NotFoundException("Stat@" + statID);
        }

        jdbcTemplateObject.update("UPDATE stats_repo SET `type`=? WHERE `stat_id`=?",
                type.toString(), statID);
    }

    public void changeStatType(String name, StatType type) {
        if (getStatByName(name) == null) {
            throw new NotFoundException("Stat@" + name);
        }

        jdbcTemplateObject.update("UPDATE stats_repo SET `type`=? WHERE `name`=?",
                type.toString(), name);
    }

    public void changeStatValue(long statID, double value) {
        if (getStatById(statID) == null) {
            throw new NotFoundException("Stat@" + statID);
        }

        jdbcTemplateObject.update("UPDATE stats_repo SET `value`=? WHERE `stat_id`=?", value, statID);
    }

    public void changeStatValue(String name, double value) {
        if (getStatByName(name) == null) {
            throw new NotFoundException("Stat@" + name);
        }

        jdbcTemplateObject.update("UPDATE stats_repo SET `value`=? WHERE `name`=?", value, name);
    }

    public void deleteStat(long statID) {
        if (getStatById(statID) == null) {
            throw new NotFoundException("Stat@" + statID);
        }

        jdbcTemplateObject.update("DELETE FROM stats_repo WHERE stat_id=?", statID);
    }

    public void deleteStat(String name) {
        if (getStatByName(name) == null) {
            throw new NotFoundException("Stat@" + name);
        }

        jdbcTemplateObject.update("DELETE FROM stats_repo WHERE name=?", name);
    }

    public List<Stat> getItemStats(String itemID) {
        return jdbcTemplateObject.query("SELECT * FROM stats_repo WHERE stat_id IN " +
                "(SELECT item_stats.stat_id FROM item_stats WHERE item_id=?)",
                new StatMapper(), itemID);
    }

    private boolean isItemStatExists(String itemID, long statID) {
        return !jdbcTemplateObject.query("SELECT item_id FROM item_stats WHERE item_id=? AND stat_id=?",
                new Object[] { itemID, statID },
                (resultSet, i) -> resultSet.getLong(1)).isEmpty();
    }

    public void addStatToItem(String itemID, long statID) {
        if (getStatById(statID) == null) {
            throw new NotFoundException("Stat@" + statID);
        }
        if (isItemStatExists(itemID, statID)) {
            throw new AlreadyExistsException("ItemStat@" + itemID);
        }

        jdbcTemplateObject.update("INSERT INTO item_stats(item_id, stat_id) VALUES (?, ?)", itemID, statID);
    }

    public void addStatToItem(String itemID, String statName) {
        Stat stat = getStatByName(statName);
        if (stat == null) {
            throw new NotFoundException("Stat@" + statName);
        }
        if (isItemStatExists(itemID, stat.getStat_id())) {
            throw new AlreadyExistsException("ItemStat@" + itemID);
        }

        jdbcTemplateObject.update("INSERT INTO item_stats(item_id, stat_id) VALUES (?, ?)",
                itemID, stat.getStat_id());
    }

    public void deleteStatFromItem(String itemID, long statID) {
        if (!isItemStatExists(itemID, statID)) {
            throw new NotFoundException("ItemStat@" + itemID);
        }

        jdbcTemplateObject.update("DELETE FROM item_stats WHERE item_id=? AND stat_id=?", itemID, statID);
    }

    public void deleteStatFromItem(String itemID, String statName) {
        Stat stat = getStatByName(statName);
        if (stat == null) {
            throw new NotFoundException("Stat@" + statName);
        }
        if (!isItemStatExists(itemID, stat.getStat_id())) {
            throw new NotFoundException("ItemStat@" + itemID);
        }

        jdbcTemplateObject.update("DELETE FROM item_stats WHERE item_id=? AND stat_id=?",
                itemID, stat.getStat_id());
    }
}
