package com.webshoprest.api.v1.services;

import com.webshoprest.domain.Item;
import com.webshoprest.domain.ItemLevel;

import java.util.List;

public interface ItemLevelService {

    List<ItemLevel> findAll();

    ItemLevel findById(Long itemLevelId);

    ItemLevel saveOrUpdateItemLevel(ItemLevel itemLevel);

    void deleteById(Long itemLevelId);

    List<Item> getItems(Long itemLevelId);

    Long numberOfItems(Long itemLevelId);

}
