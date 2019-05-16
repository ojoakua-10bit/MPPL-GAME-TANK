package com.fluxhydravault.restfrontendfx.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Player {
    private transient final StringProperty playerIdProperty;
    private transient final StringProperty usernameProperty;

    private String playerId;
    private String username;
    private String playerName;
    private int xp;
    private int rank;
    private int diamondCount;
    private int goldCount;
    private int creditBalance;
    private int inventory;
    private String avatar;
    private boolean onlineStatus;
    private boolean banStatus;

    public Player() {
        playerIdProperty = new SimpleStringProperty(null);
        usernameProperty = new SimpleStringProperty(null);
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean getBanStatus() {
        return banStatus;
    }

    public void setBanStatus(boolean banStatus) {
        this.banStatus = banStatus;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
        playerIdProperty.setValue(playerId);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        usernameProperty.setValue(username);
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getDiamondCount() {
        return diamondCount;
    }

    public void setDiamondCount(int diamondCount) {
        this.diamondCount = diamondCount;
    }

    public int getGoldCount() {
        return goldCount;
    }

    public void setGoldCount(int goldCount) {
        this.goldCount = goldCount;
    }

    public int getCreditBalance() {
        return creditBalance;
    }

    public void setCreditBalance(int creditBalance) {
        this.creditBalance = creditBalance;
    }

    public boolean getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public StringProperty getPlayerIdProperty() {
        playerIdProperty.setValue(playerId);
        return playerIdProperty;
    }

    public StringProperty getUsernameProperty() {
        usernameProperty.setValue(username);
        return usernameProperty;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerId='" + playerId + '\'' +
                ", username='" + username + '\'' +
                ", playerName='" + playerName + '\'' +
                ", xp=" + xp +
                ", rank=" + rank +
                ", diamondCount=" + diamondCount +
                ", goldCount=" + goldCount +
                ", creditBalance=" + creditBalance +
                ", inventory=" + inventory +
                ", avatar='" + avatar + '\'' +
                ", onlineStatus=" + onlineStatus +
                ", banStatus=" + banStatus +
                '}';
    }
}
