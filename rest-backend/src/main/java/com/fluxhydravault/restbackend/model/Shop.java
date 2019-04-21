package com.fluxhydravault.restbackend.model;

public class Shop {
    private String shop_item_id;
    private String item_id;
    private int gold_cost;
    private int diamond_cost;
    private int credit_cost;
    private float discount;

    public String getShop_item_id() {
        return shop_item_id;
    }

    public void setShop_item_id(String shop_item_id) {
        this.shop_item_id = shop_item_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public int getGold_cost() {
        return gold_cost;
    }

    public void setGold_cost(int gold_cost) {
        this.gold_cost = gold_cost;
    }

    public int getDiamond_cost() {
        return diamond_cost;
    }

    public void setDiamond_cost(int diamond_cost) {
        this.diamond_cost = diamond_cost;
    }

    public int getCredit_cost() {
        return credit_cost;
    }

    public void setCredit_cost(int credit_cost) {
        this.credit_cost = credit_cost;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }
}
