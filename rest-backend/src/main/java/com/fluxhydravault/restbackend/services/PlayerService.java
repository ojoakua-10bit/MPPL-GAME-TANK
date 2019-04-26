package com.fluxhydravault.restbackend.services;

import com.fluxhydravault.restbackend.model.Player;
import com.fluxhydravault.restbackend.model.PlayerInventory;

import java.util.List;

public interface PlayerService {
    Player newPlayer(String username, String password, String playerName);

    Player getPlayer(String playerID);

    Player getPlayerByUsername(String username);

    List<Player> searchPlayer(String username);

    List<Player> listPlayers();

    void deletePlayer(String playerID);

    void changePlayerUsername(String playerID, String username);

    void changePlayerPassword(String playerID, String password);

    void changePlayerName(String playerID, String playerName);

    Player authenticateUser(String username, String password);

    void changePlayerRank(String playerID, int rank);

    void changePlayerXP(String playerID, int xp);

    void changePlayerDiamond(String playerID, int amount);

    void changePlayerGold(String playerID, int amount);

    void changePlayerCredit(String playerID, int amount);

    void changePlayerInventory(String playerID, int amount);

    void changePlayerAvatar(String playerID, String location);

    void deletePlayerAvatar(String playerID);

    Integer getNumberOfFriends(String playerID);

    void addFriend(String playerID, String friendID);

    List<Player> getFriends(String playerID, int start, int limit);

    void removeFriend(String playerID, String friendID);

    void banPlayer(String playerID);

    void unBanPlayer(String playerID);

    void setPlayerOnlineStatus(String playerID, boolean onlineStatus);

    List<PlayerInventory> getPlayerItems(String playerID);

    Integer getPlayerItemsCount(String playerID);

    List<PlayerInventory> addItemToInventory(String playerID, String itemID);

    void deleteItemFromInventory(long inventoryID);
}
