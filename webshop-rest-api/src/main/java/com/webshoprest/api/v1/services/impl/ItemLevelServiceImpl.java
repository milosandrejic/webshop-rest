package com.webshoprest.api.v1.services.impl;

import com.webshoprest.api.v1.exceptions.EmptyItemsListException;
import com.webshoprest.api.v1.exceptions.ItemLevelNotFoundException;
import com.webshoprest.api.v1.services.ItemLevelService;
import com.webshoprest.domain.Item;
import com.webshoprest.domain.ItemLevel;
import com.webshoprest.repositories.ItemLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemLevelServiceImpl implements ItemLevelService {

    private ItemLevelRepository itemLevelRepository;

    @Autowired
    public ItemLevelServiceImpl(ItemLevelRepository itemLevelRepository) {
        this.itemLevelRepository = itemLevelRepository;
    }

    @Override
    public List<ItemLevel> findAll() {
        return itemLevelRepository.findAll();
    }

    @Override
    public ItemLevel findById(Long itemLevelId) {

        Optional<ItemLevel> itemLevel = itemLevelRepository.findById(itemLevelId);

        if (itemLevel.isPresent()) {
            return itemLevel.get();
        }

        throw new ItemLevelNotFoundException();

    }

    @Override
    public ItemLevel saveOrUpdateItemLevel(ItemLevel itemLevel) {
        return itemLevelRepository.save(itemLevel);
    }

    @Override
    public void deleteById(Long itemLevelId) {
        Optional<ItemLevel> itemLevel = itemLevelRepository.findById(itemLevelId);

        if (itemLevel.isPresent()) {
            itemLevelRepository.delete(itemLevel.get());
        } else {
            throw new ItemLevelNotFoundException();
        }

    }

    @Override
    public List<Item> getItems(Long itemLevelId) {

        Optional<ItemLevel> itemLevel = itemLevelRepository.findById(itemLevelId);

        if (itemLevel.isPresent()) {
            if (itemLevel.get().getItems() == null) {
                throw new EmptyItemsListException();
            }
            return itemLevel.get().getItems();

        } else {
            throw new ItemLevelNotFoundException();
        }

    }

    @Override
    public Long numberOfItems(Long itemLevelId) {
        Optional<ItemLevel> itemLevel = itemLevelRepository.findById(itemLevelId);

        if (itemLevel.isPresent()) {
            try {
                return (long) itemLevel.get().getItems().size();
            } catch (NullPointerException e) {
                return 0L;
            }

        } else {
            throw new ItemLevelNotFoundException();
        }
    }
}
