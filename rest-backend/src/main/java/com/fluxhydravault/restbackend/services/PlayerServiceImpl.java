package com.fluxhydravault.restbackend.services;

import com.fluxhydravault.restbackend.AlreadyExistsException;
import com.fluxhydravault.restbackend.InputFormatException;
import com.fluxhydravault.restbackend.NotFoundException;
import com.fluxhydravault.restbackend.model.Player;
import com.fluxhydravault.restbackend.model.PlayerInventory;
import com.fluxhydravault.restbackend.model.PlayerInventoryMapper;
import com.fluxhydravault.restbackend.model.PlayerMapper;
import com.fluxhydravault.restbackend.utils.Digestive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;
    private final String DEFAULT_AVATAR_LOCATION = "/static/default/avatar.png";

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    @Override
    public Player newPlayer(String username, String password, String playerName) {
        if (getPlayerByUsername(username) != null) {
            throw new AlreadyExistsException(username);
        }
        if (username.length() < 3) {
            throw new InputFormatException("Username should have 3 characters or more.");
        }
        if (password.length() < 8) {
            throw new InputFormatException("Password should have 8 characters or more");
        }

        String playerID;
        int i = 0;
        do {
            playerID = Digestive.sha256(username).substring(i, i + 20);
        } while(getPlayer(playerID) != null);

        jdbcTemplateObject.update("INSERT INTO player VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                playerID,
                username,
                Digestive.sha1(password),
                playerName, 0, 0, 0, 0, 0, 15,
                DEFAULT_AVATAR_LOCATION, 0, 0);
        return getPlayer(playerID);
    }

    @Override
    public Player getPlayer(String playerID) {
        Player player;
        try {
            player = jdbcTemplateObject
                    .queryForObject("SELECT * FROM player WHERE player_id=?",
                            new Object[]{playerID}, new PlayerMapper());
        } catch (IncorrectResultSizeDataAccessException e) {
            player = null;
        }
        return player;
    }

    @Override
    public Player getPlayerByUsername(String username) {
        Player player;
        try {
            player = jdbcTemplateObject
                    .queryForObject("SELECT * FROM player WHERE username=?",
                            new Object[]{username}, new PlayerMapper());
        } catch (IncorrectResultSizeDataAccessException e) {
            player = null;
        }
        return player;
    }

    @Override
    public List<Player> searchPlayer(String username) {
        String SQL = "SELECT * FROM player WHERE username LIKE ? ORDER BY username";
        return jdbcTemplateObject.query(SQL,
                new Object[]{'%' + username + '%'}, new PlayerMapper());
    }

    @Override
    public List<Player> listPlayers() {
        String SQL = "SELECT * FROM player LIMIT 300";
        return jdbcTemplateObject.query(SQL, new PlayerMapper());
    }

    @Override
    public void deletePlayer(String playerID) {
        Player temp = getPlayer(playerID);
        if (temp == null) {
            throw new NotFoundException("Player@" + playerID);
        }

        String SQL = "DELETE FROM player WHERE player_id=?";
        jdbcTemplateObject.update(SQL, playerID);
    }

    @Override
    public void changePlayerUsername(String playerID, String username) {
        Player temp = getPlayerByUsername(username);
        if (temp != null) {
            throw new AlreadyExistsException(username);
        }
        if (username.length() < 3) {
            throw new InputFormatException("Username should have 3 characters or more.");
        }

        String SQL = "UPDATE player SET username=? WHERE player_id=?";
        jdbcTemplateObject.update(SQL, username, playerID);
    }

    @Override
    public void changePlayerPassword(String playerID, String password) {
        if (password.length() < 8) {
            throw new InputFormatException("Password should have 8 characters or more");
        }

        String SQL = "UPDATE player SET `password`=? WHERE player_id=?";
        jdbcTemplateObject.update(SQL, Digestive.sha1(password), playerID);
    }

    @Override
    public void changePlayerName(String playerID, String playerName){
        String SQL = "UPDATE player SET player_name=? WHERE player_id=?";
        jdbcTemplateObject.update(SQL, playerName, playerID);
    }

    @Override
    public Player authenticateUser(String username, String password) {
        String SQL = "SELECT * FROM player WHERE username=? and password=?";
        Player player;

        try {
            player = jdbcTemplateObject.queryForObject(SQL,
                    new Object[]{username, Digestive.sha1(password)},
                    new PlayerMapper());
        } catch (IncorrectResultSizeDataAccessException e) {
            player = null;
        }

        return player;
    }

    @Override
    public void changePlayerRank(String playerID, int rank) {
        String SQL = "UPDATE player SET `rank`=? WHERE `player_id`=?";
        jdbcTemplateObject.update(SQL, rank, playerID);
    }

    @Override
    public void changePlayerXP(String playerID, int xp) {
        String SQL = "UPDATE player SET `xp`=? WHERE `player_id`=?";
        jdbcTemplateObject.update(SQL, xp, playerID);
    }

    @Override
    public void changePlayerDiamond(String playerID, int amount) {
        String SQL = "UPDATE player SET diamond_count=? WHERE player_id=?";
        jdbcTemplateObject.update(SQL, amount, playerID);
    }

    @Override
    public void changePlayerGold(String playerID, int amount) {
        String SQL = "UPDATE player SET gold_count=? WHERE player_id=?";
        jdbcTemplateObject.update(SQL, amount, playerID);
    }

    @Override
    public void changePlayerCredit(String playerID, int amount) {
        String SQL = "UPDATE player SET credit_balance=? WHERE player_id=?";
        jdbcTemplateObject.update(SQL, amount, playerID);
    }

    @Override
    public void changePlayerInventory(String playerID, int amount) {
        String SQL = "UPDATE player SET inventory=? WHERE player_id=?";
        jdbcTemplateObject.update(SQL, amount, playerID);
    }

    @Override
    public void changePlayerAvatar(String playerID, String location) {
        String SQL = "UPDATE player SET avatar=? WHERE player_id=?";
        jdbcTemplateObject.update(SQL, location, playerID);
    }

    @Override
    public void deletePlayerAvatar(String playerID) {
        String SQL = "UPDATE player SET avatar=? WHERE player_id=?";
        jdbcTemplateObject.update(SQL, DEFAULT_AVATAR_LOCATION, playerID);
    }

    private boolean isFriend(String playerID, String friendID) {
        Integer test = jdbcTemplateObject
                .queryForObject("SELECT friend_id FROM friend WHERE player_id=? AND friend_player_id=?",
                        new Object[]{playerID, friendID}, (resultSet, i) -> resultSet.getInt(1));
        return (test == null);
    }

    @Override
    public Integer getNumberOfFriends(String playerID) {
        return jdbcTemplateObject.queryForObject("SELECT COUNT(*) FROM friend WHERE player_id=?",
                new Object[]{playerID}, (resultSet, i) -> resultSet.getInt(1));
    }

    @Override
    public void addFriend(String playerID, String friendID) {
        if (isFriend(playerID, friendID)) {
            throw new AlreadyExistsException("friend_relation");
        }

        jdbcTemplateObject.update("INSERT INTO friend(player_id, friend_player_id) VALUES (?, ?)",
                playerID, friendID);
    }

    @Override
    public List<Player> getFriends(String playerID, int start, int limit) {
        String SQL = "SELECT * FROM player WHERE player_id IN " +
                "(SELECT friend_player_id FROM friend WHERE player_id=?)" +
                "ORDER BY username LIMIT ?, ?";
        return jdbcTemplateObject.query(SQL, new Object[]{playerID, start, limit}, new PlayerMapper());
    }

    @Override
    public void removeFriend(String playerID, String friendID) {
        if (!isFriend(playerID, friendID)) {
            throw new NotFoundException("Friend relation not found");
        }

        String SQL = "DELETE FROM friend WHERE player_id=? AND friend_player_id=?";
        jdbcTemplateObject.update(SQL, playerID, friendID);
    }

    @Override
    public void banPlayer(String playerID) {
        String SQL = "UPDATE player SET ban_status='1' WHERE player_id=?";
        if (isBannedPlayer(playerID)) throw new AlreadyExistsException("ban_status");
        jdbcTemplateObject.update(SQL, playerID);
    }

    @Override
    public void unBanPlayer(String playerID) {
        String SQL = "UPDATE player SET ban_status='0' WHERE player_id=?";
        if (!isBannedPlayer(playerID)) throw new AlreadyExistsException("ban_status");
        jdbcTemplateObject.update(SQL, playerID);
    }

    private boolean isBannedPlayer(String playerID) {
        Player tmp = getPlayer(playerID);
        return tmp.getBan_status();
    }

    @Override
    public void setPlayerOnlineStatus(String playerID, boolean onlineStatus) {
        String SQL = "UPDATE player SET online_status=? WHERE player_id=?";
        jdbcTemplateObject.update(SQL, onlineStatus ? 1 : 0, playerID);
    }

    @Override
    public List<PlayerInventory> getPlayerItems(String playerID) {
        return jdbcTemplateObject.query("SELECT p.inventory_id, i.* FROM item i, player_inventory p " +
                        "WHERE p.item_id = i.item_id AND p.player_id=?",
                new PlayerInventoryMapper(), playerID);
    }

    @Override
    public Integer getPlayerItemsCount(String playerID) {
        return jdbcTemplateObject.queryForObject("SELECT COUNT(*) FROM item WHERE item_id IN " +
                        "(SELECT player_inventory.item_id FROM player_inventory WHERE player_id=?)",
                (resultSet, i) -> resultSet.getInt(1), playerID);
    }

    @Override
    public List<PlayerInventory> addItemToInventory(String playerID, String itemID) {
        Player player = getPlayer(playerID);
        if (player == null) {
            throw new NotFoundException("Player@" + playerID);
        }

        int currentInventorySize = getPlayerItemsCount(playerID);
        if (currentInventorySize >= player.getInventory()) {
            throw new RuntimeException("Inventory already full.");
        }

        jdbcTemplateObject.update("INSERT INTO player_inventory(player_id, item_id) VALUES (?, ?)", player, itemID);
        return getPlayerItems(playerID);
    }

    @Override
    public void deleteItemFromInventory(long inventoryID) {
        jdbcTemplateObject.update("DELETE FROM player_inventory WHERE inventory_id=?", inventoryID);
    }
}
