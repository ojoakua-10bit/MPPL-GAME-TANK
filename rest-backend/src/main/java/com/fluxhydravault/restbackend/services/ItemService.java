package com.fluxhydravault.restbackend.services;

import com.fluxhydravault.restbackend.model.Item;
import com.fluxhydravault.restbackend.model.ItemCategory;

import java.util.List;

public interface ItemService {
    Item newItem(ItemCategory category, String itemName, String description, String location);

    Item getItem(String itemID);

    Item getItemByName(String itemName);

    List<Item> searchItemByName(String itemName);

    List<Item> getItems(int start, int limit);

    Integer getNumberOfItems();

    void changeItemName(String itemID, String itemName);

    void changeItemCategory(String itemID, ItemCategory category);

    void changeItemDescription(String itemID, String description);

    void changeItemModelLocation(String itemID, String location);

    void deleteItem(String itemID);
}
