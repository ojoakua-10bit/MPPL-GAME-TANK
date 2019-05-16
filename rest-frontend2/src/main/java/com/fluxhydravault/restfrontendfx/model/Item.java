package com.fluxhydravault.restfrontendfx.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Item {
    private transient StringProperty itemIdProperty;
    private transient StringProperty itemNameProperty;

    private String itemId;
    private ItemCategory itemCategory;
    private String itemName;
    private String description;
    private String modelLocation;

    public Item() {
        itemIdProperty = new SimpleStringProperty(null);
        itemNameProperty = new SimpleStringProperty(null);
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        itemIdProperty.setValue(itemId);
        this.itemId = itemId;
    }

    public ItemCategory getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        itemNameProperty.set(itemName);
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModelLocation() {
        return modelLocation;
    }

    public void setModelLocation(String modelLocation) {
        this.modelLocation = modelLocation;
    }

    public StringProperty getItemIdProperty() {
        itemIdProperty.setValue(itemId);
        return itemIdProperty;
    }

    public StringProperty getItemNameProperty() {
        itemNameProperty.setValue(itemName);
        return itemNameProperty;
    }
}
