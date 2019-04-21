package com.fluxhydravault.restbackend.dao;

import com.fluxhydravault.restbackend.model.Item;
import com.fluxhydravault.restbackend.model.ItemCategory;
import com.fluxhydravault.restbackend.model.ItemMapper;
import com.fluxhydravault.restbackend.utils.Digestive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
public class ItemDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    public Item newItem(ItemCategory category, String itemName, String description, String location) {
        String itemID = Digestive.sha256(Double.toString(Math.random())).substring(0, 20);

        if (location == null) {
            jdbcTemplateObject.update("INSERT INTO item(item_id, item_category, item_name, `description`) " +
                    "VALUES (?, ?, ?, ?)", itemID, category.toString(), itemName, description);
        }
        else {
            jdbcTemplateObject.update("INSERT INTO item VALUES (?, ?, ?, ?, ?)",
                    itemID, category.toString(), itemName, description);
        }

        return getItem(itemID);
    }

    private Item getItem(String itemID) {
        Item temp;
        try {
            temp = jdbcTemplateObject.queryForObject("SELECT * FROM item WHERE item_id=?",
                    new Object[]{ itemID }, new ItemMapper());
        } catch (IncorrectResultSetColumnCountException e) {
            temp = null;
        }
        return temp;
    }

    public Item getItemByName(String itemName) {
        Item temp;
        try {
            temp = jdbcTemplateObject.queryForObject("SELECT * FROM item WHERE item_name LIKE ?",
                    new Object[]{ '%' + itemName + '%' }, new ItemMapper());
        } catch (IncorrectResultSetColumnCountException e) {
            temp = null;
        }
        return temp;
    }

    public List<Item> getAllItems() {
        return jdbcTemplateObject.query("SELECT * FROM item", new ItemMapper());
    }
}
