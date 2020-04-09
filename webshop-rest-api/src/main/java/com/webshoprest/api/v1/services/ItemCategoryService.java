package com.webshoprest.api.v1.services;

import com.webshoprest.domain.Item;
import com.webshoprest.domain.ItemCategory;

import java.util.List;

public interface ItemCategoryService {

    List<ItemCategory> findAll();

    ItemCategory findById(Long itemCategoryId);

    ItemCategory saveOrUpdateCategory(ItemCategory itemCategory);

    void deleteById(Long itemCategoryId);

    List<Item> getItems(Long itemCategoryId);

    Long numberOfItems(Long itemCategoryId);

}
