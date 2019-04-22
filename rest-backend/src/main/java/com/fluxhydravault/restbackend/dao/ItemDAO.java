package com.fluxhydravault.restbackend.dao;

import com.fluxhydravault.restbackend.NotFoundException;
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

    public Item getItem(String itemID) {
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
            temp = jdbcTemplateObject.queryForObject("SELECT * FROM item WHERE item_name=?",
                    new Object[]{ '%' + itemName + '%' }, new ItemMapper());
        } catch (IncorrectResultSetColumnCountException e) {
            temp = null;
        }
        return temp;
    }

    public List<Item> searchItemByName(String itemName) {
        return jdbcTemplateObject.query("SELECT * FROM item WHERE item_name LIKE ?",
                new Object[]{ '%' + itemName + '%' }, new ItemMapper());
    }

    public List<Item> getItems(int start, int limit) {
        return jdbcTemplateObject.query("SELECT * FROM item LIMIT ?, ?",
                new ItemMapper(), start, limit);
    }

    public Integer getNumberOfItems() {
        return jdbcTemplateObject.queryForObject("SELECT COUNT(*) FROM item",
                (resultSet, i) -> resultSet.getInt(1));
    }

    public void changeItemName(String itemID, String itemName) {
        String SQL = "UPDATE item SET `item_name`=? WHERE `item_id`=?";
        jdbcTemplateObject.update(SQL, itemName, itemID);
    }

    public void changeItemCategory(String itemID, ItemCategory category) {
        String SQL = "UPDATE item SET `item_category`=? WHERE `item_id`=?";
        jdbcTemplateObject.update(SQL, category.toString(), itemID);
    }


    public void changeItemDescription(String itemID, String description) {
        String SQL = "UPDATE item SET `description`=? WHERE `item_id`=?";
        jdbcTemplateObject.update(SQL, description, itemID);
    }

    public void changeItemModelLocation(String itemID, String location) {
        String SQL = "UPDATE item SET `model_location`=? WHERE `item_id`=?";
        jdbcTemplateObject.update(SQL, location, itemID);
    }

    public void deleteItem(String itemID) {
        Item temp = getItem(itemID);
        if (temp == null) {
            throw new NotFoundException("Item@" + itemID);
        }

        String SQL = "DELETE FROM item WHERE item_id=?";
        jdbcTemplateObject.update(SQL, itemID);
    }
}
