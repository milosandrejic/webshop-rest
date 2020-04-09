package com.webshoprest.api.v1.services;

import com.webshoprest.domain.Item;
import com.webshoprest.domain.ItemCategory;
import com.webshoprest.domain.ItemLevel;
import com.webshoprest.domain.OrderedItem;

import java.util.List;

public interface ItemService {

    List<Item> findAllItems();

    Item findById(Long itemId);

    Item saveOrUpdateItem(Item item);

    void deleteItemById(Long itemId);

    ItemCategory getCategory(Long itemId);

    ItemLevel getLevel(Long itemId);

    List<OrderedItem> topSellingItems(Long limit);

}
