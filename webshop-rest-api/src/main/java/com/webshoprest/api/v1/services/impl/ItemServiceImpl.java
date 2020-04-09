package com.webshoprest.api.v1.services.impl;

import com.webshoprest.api.v1.exceptions.ItemNotFoundException;
import com.webshoprest.api.v1.services.ItemService;
import com.webshoprest.domain.Item;
import com.webshoprest.domain.ItemCategory;
import com.webshoprest.domain.ItemLevel;
import com.webshoprest.domain.OrderedItem;
import com.webshoprest.repositories.ItemRepository;
import com.webshoprest.repositories.OrderedItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private ItemRepository itemRepository;
    private OrderedItemRepository orderedItemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, OrderedItemRepository orderedItemRepository) {
        this.itemRepository = itemRepository;
        this.orderedItemRepository = orderedItemRepository;
    }

    @Override
    public List<Item> findAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public Item findById(Long itemId) {

        Optional<Item> item = itemRepository.findById(itemId);

        if (item.isPresent()) {
            return item.get();
        }

        throw new ItemNotFoundException();

    }

    @Override
    public Item saveOrUpdateItem(Item item) {

        if (item.getItemId() != null) {
            if (!itemRepository.existsById(item.getItemId())) {
                throw new ItemNotFoundException();
            }
        }

        if (item.getQty() == null) {
            item.setQty(0L);
        }

        if (item.getDiscount() == null) {
            item.setDiscount(0.0);
        }

        return itemRepository.save(item);
    }

    @Override
    public void deleteItemById(Long itemId) {

        Optional<Item> item = itemRepository.findById(itemId);

        if (item.isPresent()) {
            itemRepository.delete(item.get());
        } else {
            throw new ItemNotFoundException();
        }

    }

    @Override
    public ItemCategory getCategory(Long itemId) {

        Optional<Item> item = itemRepository.findById(itemId);

        if (item.isPresent()) {
            return item.get().getItemCategory();
        }

        throw new ItemNotFoundException();

    }

    @Override
    public ItemLevel getLevel(Long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);

        if (item.isPresent()) {
            return item.get().getItemLevel();
        }

        throw new ItemNotFoundException();
    }

    @Override
    public List<OrderedItem> topSellingItems(Long limit) {

        List<OrderedItem> uniqueItemsList = new ArrayList<>(removeDuplicates(orderedItemRepository.findAll()));

        uniqueItemsList.forEach(e -> e.setIncludedInOrderCount(e.getOrderItemOrders().size()));

        return uniqueItemsList
                .stream()
                .filter(item -> item.getIncludedInOrderCount() != null)
                .sorted(Comparator.comparingLong(OrderedItem::getIncludedInOrderCount).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }


    private List<OrderedItem> removeDuplicates(List<OrderedItem> listWithDuplicates) {
        Map<Integer, OrderedItem> orderMap = new HashMap<>();

        listWithDuplicates
                .forEach(item -> orderMap.putIfAbsent(item.getOrderedItemName().hashCode(), item));

        return new ArrayList<>(orderMap.values());
    }
}
