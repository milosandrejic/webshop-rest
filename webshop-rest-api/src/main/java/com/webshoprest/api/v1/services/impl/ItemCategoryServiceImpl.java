package com.webshoprest.api.v1.services.impl;

import com.webshoprest.api.v1.exceptions.EmptyItemsListException;
import com.webshoprest.api.v1.exceptions.ItemCategoryNotFoundException;
import com.webshoprest.api.v1.services.ItemCategoryService;
import com.webshoprest.domain.Item;
import com.webshoprest.domain.ItemCategory;
import com.webshoprest.repositories.ItemCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemCategoryServiceImpl implements ItemCategoryService {

    private ItemCategoryRepository itemCategoryRepository;

    @Autowired
    public ItemCategoryServiceImpl(ItemCategoryRepository itemCategoryRepository) {
        this.itemCategoryRepository = itemCategoryRepository;
    }

    @Override
    public List<ItemCategory> findAll() {
        return itemCategoryRepository.findAll();
    }

    @Override
    public ItemCategory findById(Long itemCategoryId) {

        Optional<ItemCategory> itemCategory = itemCategoryRepository.findById(itemCategoryId);

        if (itemCategory.isPresent()) {
            return itemCategory.get();
        } else {
            throw new ItemCategoryNotFoundException();
        }

    }

    @Override
    public ItemCategory saveOrUpdateCategory(ItemCategory itemCategory) {

        if(itemCategory.getItemCategoryId() != null && itemCategoryRepository.existsById(itemCategory.getItemCategoryId())){
            throw new ItemCategoryNotFoundException();
        }

        return itemCategoryRepository.save(itemCategory);
    }

    @Override
    public void deleteById(Long itemCategoryId) {

        if (itemCategoryRepository.existsById(itemCategoryId)) {
            itemCategoryRepository.deleteById(itemCategoryId);
        } else {
            throw new ItemCategoryNotFoundException();
        }

    }

    @Override
    public List<Item> getItems(Long itemCategoryId) {

        Optional<ItemCategory> itemCategory = itemCategoryRepository.findById(itemCategoryId);

        if (itemCategory.isPresent()) {
            if (itemCategory.get().getItems() == null || itemCategory.get().getItems().isEmpty()) {
                throw new EmptyItemsListException();
            }
            return itemCategory.get().getItems();
        } else {
            throw new ItemCategoryNotFoundException();
        }
    }


    @Override
    public Long numberOfItems(Long itemCategoryId) {

        Optional<ItemCategory> itemCategory = itemCategoryRepository.findById(itemCategoryId);

        if (itemCategory.isPresent()) {
            try {
                return (long) itemCategory.get().getItems().size();
            } catch (NullPointerException e) {
                return 0L;
            }
        } else {
            throw new ItemCategoryNotFoundException();
        }

    }
}
