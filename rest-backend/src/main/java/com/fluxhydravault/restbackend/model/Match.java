package com.fluxhydravault.restbackend.model;

public class Match {
    private String match_id;
    private String player_id;
    private byte match_status;
    private int score;
    private int total_damage;
    private int gold_gained;
    private String item_gained;

    public String getMatch_id() {
        return match_id;
    }

    public void setMatch_id(String match_id) {
        this.match_id = match_id;
    }

    public String getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(String player_id) {
        this.player_id = player_id;
    }

    public byte getMatch_status() {
        return match_status;
    }

    public void setMatch_status(byte match_status) {
        this.match_status = match_status;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotal_damage() {
        return total_damage;
    }

    public void setTotal_damage(int total_damage) {
        this.total_damage = total_damage;
    }

    public int getGold_gained() {
        return gold_gained;
    }

    public void setGold_gained(int gold_gained) {
        this.gold_gained = gold_gained;
    }

    public String getItem_gained() {
        return item_gained;
    }

    public void setItem_gained(String item_gained) {
        this.item_gained = item_gained;
    }
}
