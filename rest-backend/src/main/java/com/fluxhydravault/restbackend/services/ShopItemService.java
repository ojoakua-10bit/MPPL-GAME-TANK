package com.fluxhydravault.restbackend.services;

import com.fluxhydravault.restbackend.model.ShopItem;

import java.util.List;

public interface ShopItemService {
    ShopItem newShopItem(String itemID, int gold, int diamond, int credit, float discount);

    ShopItem getShopItem(String shopID);

    ShopItem getShopItemByItemName(String itemName);

    List<ShopItem> searchShopItemByItemName(String itemName);

    List<ShopItem> getShopItems(int start, int limit);

    Integer getNumberOfShopItems();

    void changeItemID(String shopID, String itemID);

    void changeGoldCost(String shopID, int amount);

    void changeDiamondCost(String shopID, int amount);

    void changeCreditCost(String shopID, int amount);

    void changeDiscount(String shopID, float amount);

    void deleteShopItem(String shopID);
}
