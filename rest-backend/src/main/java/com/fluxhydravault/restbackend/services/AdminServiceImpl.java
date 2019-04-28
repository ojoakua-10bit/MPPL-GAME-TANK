package com.fluxhydravault.restbackend.services;

import com.fluxhydravault.restbackend.AlreadyExistsException;
import com.fluxhydravault.restbackend.InputFormatException;
import com.fluxhydravault.restbackend.NotFoundException;
import com.fluxhydravault.restbackend.model.Admin;
import com.fluxhydravault.restbackend.model.AdminMapper;
import com.fluxhydravault.restbackend.utils.Digestive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;
    private final String DEFAULT_AVATAR_LOCATION = "/static/default/avatar.png";

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    @Override
    public Admin newAdmin(String username, String password, String adminName) {
        if (getAdminByUsername(username) != null) {
            throw new AlreadyExistsException(username);
        }
        if (username.length() < 3) {
            throw new InputFormatException("Username should have 3 characters or more.");
        }
        if (password.length() < 8) {
            throw new InputFormatException("Password should have 8 characters or more");
        }

        String adminID;
        int i = 0;
        do {
            adminID = Digestive.sha256(username).substring(i, i + 13);
        } while(getAdmin(adminID) != null);

        jdbcTemplateObject.update("INSERT INTO `admin` VALUES (?, ?, ?, ?, ?)",
                adminID,
                username,
                Digestive.sha1(password),
                adminName,
                DEFAULT_AVATAR_LOCATION);
        return getAdmin(adminID);
    }

    @Override
    public Admin getAdmin(String adminID) {
        Admin admin;
        try {
            admin = jdbcTemplateObject
                    .queryForObject("SELECT * FROM `admin` WHERE admin_id=?",
                            new Object[]{adminID}, new AdminMapper());
        } catch (IncorrectResultSizeDataAccessException e) {
            admin = null;
        }
        return admin;
    }

    @Override
    public Admin getAdminByUsername(String username) {
        Admin admin;
        try {
            admin = jdbcTemplateObject
                    .queryForObject("SELECT * FROM `admin` WHERE username=?",
                            new Object[]{username}, new AdminMapper());
        } catch (IncorrectResultSizeDataAccessException e) {
            admin = null;
        }
        return admin;
    }

    @Override
    public List<Admin> searchAdmin(String username) {
        String SQL = "SELECT * FROM `admin` WHERE username LIKE ? ORDER BY username";
        return jdbcTemplateObject.query(SQL,
                new Object[]{'%' + username + '%'}, new AdminMapper());
    }

    @Override
    public List<Admin> listAdmins() {
        String SQL = "SELECT * FROM `admin` LIMIT 300";
        return jdbcTemplateObject.query(SQL, new AdminMapper());
    }

    @Override
    public void deleteAdmin(String adminID) {
        Admin temp = getAdmin(adminID);
        if (temp == null) {
            throw new NotFoundException("Player@" + adminID);
        }

        String SQL = "DELETE FROM `admin` WHERE admin_id=?";
        jdbcTemplateObject.update(SQL, adminID);
    }

    @Override
    public void changeAdminUsername(String adminID, String username) {
        Admin temp = getAdminByUsername(username);
        if (temp != null) {
            throw new AlreadyExistsException(username);
        }
        if (username.length() < 3) {
            throw new InputFormatException("Username should have 3 characters or more.");
        }

        String SQL = "UPDATE `admin` SET username=? WHERE admin_id=?";
        jdbcTemplateObject.update(SQL, username, adminID);
    }

    @Override
    public void changeAdminPassword(String adminID, String password) {
        if (password.length() < 8) {
            throw new InputFormatException("Password should have 8 characters or more");
        }

        String SQL = "UPDATE `admin` SET `password`=? WHERE admin_id=?";
        jdbcTemplateObject.update(SQL, Digestive.sha1(password), adminID);
    }

    @Override
    public void changeAdminName(String adminID, String adminName) {
        String SQL = "UPDATE `admin` SET `admin_name`=? WHERE admin_id=?";
        jdbcTemplateObject.update(SQL, adminID, adminID);
    }

    @Override
    public void changePlayerAvatar(String adminID, String location) {
        String SQL = "UPDATE `admin` SET avatar=? WHERE admin_id=?";
        jdbcTemplateObject.update(SQL, DEFAULT_AVATAR_LOCATION, adminID);
    }

    @Override
    public Admin authenticateUser(String username, String password) {
        String SQL = "SELECT * FROM `admin` WHERE username=? and password=?";
        Admin admin;

        try {
            admin = jdbcTemplateObject.queryForObject(SQL,
                    new Object[]{username, Digestive.sha1(password)},
                    new AdminMapper());
        } catch (IncorrectResultSizeDataAccessException e) {
            admin = null;
        }

        return admin;
    }

    @Override
    public void deleteAdminAvatar(String adminID) {
        String SQL = "UPDATE `admin` SET avatar=? WHERE admin_id=?";
        jdbcTemplateObject.update(SQL, DEFAULT_AVATAR_LOCATION, adminID);
    }
}
