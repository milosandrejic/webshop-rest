package com.webshoprest.api.v1.services;

import com.webshoprest.api.v1.exceptions.EmptyItemsListException;
import com.webshoprest.api.v1.exceptions.ItemCategoryNotFoundException;
import com.webshoprest.api.v1.services.impl.ItemCategoryServiceImpl;
import com.webshoprest.domain.Item;
import com.webshoprest.domain.ItemCategory;
import com.webshoprest.repositories.ItemCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

class ItemCategoryServiceTest {

    @Mock
    private ItemCategoryRepository itemCategoryRepository;


    @InjectMocks
    private ItemCategoryServiceImpl itemCategoryService;

    private ItemCategory itemCategory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        itemCategory = new ItemCategory();
        itemCategory.setItemCategoryId(1L);
        itemCategory.setItemCategoryName("DRY BAKING GOODS");

    }


    @Test
    void findAll() {
        List<ItemCategory> itemCategories = new ArrayList<>();
        itemCategories.add(itemCategory);

        given(itemCategoryRepository.findAll()).willReturn(itemCategories);

        List<ItemCategory> categories =  itemCategoryService.findAll();

        assertThat(categories).isNotNull();
        assertThat(categories.size()).isEqualTo(itemCategories.size());
        assertThat(categories.get(0).getItemCategoryId()).isEqualTo(itemCategories.get(0).getItemCategoryId());

    }

    @Test
    void findByIdPresent() {

        given(itemCategoryRepository.findById(anyLong())).willReturn(Optional.of(itemCategory));

        itemCategory = itemCategoryService.findById(1L);

        assertThat(itemCategory).isNotNull();
        assertThat(itemCategory.getItemCategoryId()).isEqualTo(itemCategory.getItemCategoryId());
        assertThat(itemCategory.getItemCategoryName()).isEqualTo("DRY BAKING GOODS");
    }

    @Test
    void findByNameNotPresent() {
        given(itemCategoryRepository.findByItemCategoryNameIgnoreCase(anyString())).willReturn(Optional.empty());

        assertThrows(ItemCategoryNotFoundException.class, () ->itemCategoryService.findById(1L));

    }
    @Test
    void saveOrUpdateCategory() {

        given(itemCategoryRepository.save(any(ItemCategory.class))).willReturn(itemCategory);
        given(itemCategoryRepository.existsById(anyLong())).willReturn(false);

        itemCategory = itemCategoryService.saveOrUpdateCategory(itemCategory);

        assertThat(itemCategory).isNotNull();
    }

    @Test
    void saveOrUpdateCategory_itemCategoryNotFound() {

        given(itemCategoryRepository.save(any(ItemCategory.class))).willReturn(itemCategory);
        given(itemCategoryRepository.existsById(anyLong())).willReturn(true);
        assertThrows(ItemCategoryNotFoundException.class, () -> itemCategoryService.saveOrUpdateCategory(itemCategory));
    }

    @Test
    void deleteByIdPresent() {

        given(itemCategoryRepository.existsById(anyLong())).willReturn(true);

        itemCategoryService.deleteById(1L);

        then(itemCategoryRepository).should(times(1)).existsById(1L);
        then(itemCategoryRepository).should(times(1)).deleteById(1L);

    }

    @Test
    void deleteByIdNotPresent() {
        given(itemCategoryRepository.existsById(anyLong())).willReturn(false);

        assertThrows(ItemCategoryNotFoundException.class, () -> itemCategoryService.deleteById(1L));

    }

    @Test
    void getItems_ItemCategoryPresent() {
        List<ItemCategory> categories = new ArrayList<>();
        Item item1 = new Item();
        Item item2 = new Item();

        List<Item> items  = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        itemCategory.setItems(items);

        given(itemCategoryRepository.findById(anyLong())).willReturn(Optional.of(itemCategory));

        List<Item> itemsList = itemCategoryService.getItems(1L);

        assertThat(itemsList).isNotNull();
        assertThat(itemsList.size()).isEqualTo(items.size());
        then(itemCategoryRepository).should(times(1)).findById(1L);
    }

    @Test
    void getItemsItemCategoryNotPresent() {
        given(itemCategoryRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(ItemCategoryNotFoundException.class, () -> itemCategoryService.getItems(anyLong()));
        then(itemCategoryRepository).should(times(1)).findById(anyLong());

    }

    @Test
    void getItemsItemCategoryPresentItemsEmpty() {
        itemCategory.setItems(null);
        given(itemCategoryRepository.findById(anyLong())).willReturn(Optional.of(itemCategory));

        assertThrows(EmptyItemsListException.class, () -> itemCategoryService.getItems(anyLong()));
        then(itemCategoryRepository).should(times(1)).findById(anyLong());
    }

    @Test
    void numberOfItemsPresent() {

        Item item1 = new Item();
        Item item2 = new Item();

        List<Item> items  = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        itemCategory.setItems(items);

        given(itemCategoryRepository.findById(anyLong())).willReturn(Optional.of(itemCategory));

        Long count = itemCategoryService.numberOfItems(1L);

        assertThat(count).isEqualTo(2L);

    }

    @Test
    void numberOfItemsNotPresent() {

        Item item1 = new Item();
        Item item2 = new Item();

        Set<Item> items  = new HashSet<>();
        items.add(item1);
        items.add(item2);

        given(itemCategoryRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(ItemCategoryNotFoundException.class, () -> itemCategoryService.numberOfItems(anyLong()));

        then(itemCategoryRepository).should().findById(any());

    }

    @Test
    void numberOfItems_itemsNull() {

        given(itemCategoryRepository.findById(anyLong())).willReturn(Optional.of(itemCategory));

        Long count = itemCategoryService.numberOfItems(1L);

        assertThat(count).isEqualTo(0L);

    }
}