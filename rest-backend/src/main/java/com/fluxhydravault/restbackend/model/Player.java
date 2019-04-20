package com.fluxhydravault.restbackend.model;

public class Player {
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
    private boolean ban_status;

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
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
