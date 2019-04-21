package com.fluxhydravault.restbackend.model;

public class Item {
    private String item_id;
    private ItemCategory item_category;
    private String item_name;
    private String description;
    private String model_location;

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public ItemCategory getItem_category() {
        return item_category;
    }

    public void setItem_category(ItemCategory item_category) {
        this.item_category = item_category;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModel_location() {
        return model_location;
    }

    public void setModel_location(String model_location) {
        this.model_location = model_location;
    }
}
