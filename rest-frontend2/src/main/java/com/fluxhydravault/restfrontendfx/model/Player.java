package com.fluxhydravault.restfrontendfx.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Player {
    private transient final StringProperty playerIdProperty;
    private transient final StringProperty usernameProperty;

    private String player_id;
    private String username;
    private String player_name;
    private int xp;
    private int rank;
    private int diamond_count;
    private int gold_count;
    private int credit_balance;
    private int inventory;
    private String avatar;
    private boolean online_status;
    private boolean ban_status;

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

    public boolean getBan_status() {
        return ban_status;
    }

    public void setBan_status(boolean ban_status) {
        this.ban_status = ban_status;
    }

    public String getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(String player_id) {
        this.player_id = player_id;
        playerIdProperty.setValue(player_id);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        usernameProperty.setValue(username);
    }

    public String getPlayer_name() {
        return player_name;
    }

    public void setPlayer_name(String player_name) {
        this.player_name = player_name;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getDiamond_count() {
        return diamond_count;
    }

    public void setDiamond_count(int diamond_count) {
        this.diamond_count = diamond_count;
    }

    public int getGold_count() {
        return gold_count;
    }

    public void setGold_count(int gold_count) {
        this.gold_count = gold_count;
    }

    public int getCredit_balance() {
        return credit_balance;
    }

    public void setCredit_balance(int credit_balance) {
        this.credit_balance = credit_balance;
    }

    public boolean getOnline_status() {
        return online_status;
    }

    public void setOnline_status(boolean online_status) {
        this.online_status = online_status;
    }

    public StringProperty getPlayerIdProperty() {
        playerIdProperty.setValue(player_id);
        return playerIdProperty;
    }

    public StringProperty getUsernameProperty() {
        usernameProperty.setValue(username);
        return usernameProperty;
    }

    @Override
    public String toString() {
        return "Player{" +
                "player_id='" + player_id + '\'' +
                ", username='" + username + '\'' +
                ", player_name='" + player_name + '\'' +
                ", xp=" + xp +
                ", rank=" + rank +
                ", diamond_count=" + diamond_count +
                ", gold_count=" + gold_count +
                ", credit_balance=" + credit_balance +
                ", inventory=" + inventory +
                ", avatar='" + avatar + '\'' +
                ", online_status=" + online_status +
                ", ban_status=" + ban_status +
                '}';
    }
}