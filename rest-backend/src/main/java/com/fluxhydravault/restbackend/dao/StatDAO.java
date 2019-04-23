package com.fluxhydravault.restbackend.dao;

import com.fluxhydravault.restbackend.model.Stat;
import com.fluxhydravault.restbackend.model.StatMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Stat> getStats(String itemID) {
        return jdbcTemplateObject.query("SELECT * FROM stats_repo WHERE stat_id IN " +
                "(SELECT item_stats.stat_id FROM item_stats WHERE item_id=?)",
                new StatMapper(), itemID);
    }
}
