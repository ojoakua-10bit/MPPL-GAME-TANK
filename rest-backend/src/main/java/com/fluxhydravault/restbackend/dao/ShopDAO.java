package com.fluxhydravault.restbackend.dao;

import com.fluxhydravault.restbackend.model.Shop;
import com.fluxhydravault.restbackend.model.ShopMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ShopDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    public Shop newShopItem(String itemID, int gold, int diamond, int credit, float discount) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String shopID = format.format(new Date()) + '-' + itemID;

        jdbcTemplateObject.update("INSERT INTO shop_item VALUES (?, ?, ?, ?, ?, ?)",
                shopID, itemID, gold, diamond, credit, discount);

        return getShopItem(shopID);
    }

    public Shop getShopItem(String shopID) {
        Shop temp;
        try {
            temp = jdbcTemplateObject.queryForObject("SELECT * FROM shop_item WHERE shop_item_id=?",
                    new Object[]{ shopID }, new ShopMapper());
        } catch (IncorrectResultSetColumnCountException e) {
            temp = null;
        }
        return temp;
    }
}
