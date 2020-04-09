package com.webshoprest.api.v1.services;

import com.webshoprest.api.v1.exceptions.EmptyItemsListException;
import com.webshoprest.api.v1.exceptions.ItemLevelNotFoundException;
import com.webshoprest.api.v1.services.impl.ItemLevelServiceImpl;
import com.webshoprest.domain.Item;
import com.webshoprest.domain.ItemLevel;
import com.webshoprest.domain.enums.ItemLevels;
import com.webshoprest.repositories.ItemLevelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

class ItemLevelServiceTest {

    @Mock
    private ItemLevelRepository itemLevelRepository;

    @InjectMocks
    private ItemLevelServiceImpl itemLevelService;


    private ItemLevel itemLevel;

    private List<ItemLevel> itemLevels;

    private List<Item> items;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        itemLevel = new ItemLevel();
        itemLevel.setLevel(ItemLevels.SILVER);
        itemLevel.setItemLevelId(1L);
    }

    @Test
    void findAll() {
        itemLevels = new ArrayList<>();
        itemLevels.add(itemLevel);

        given(itemLevelRepository.findAll()).willReturn(itemLevels);

        List<ItemLevel> listOfItemLevels = itemLevelService.findAll();

        assertThat(listOfItemLevels).isNotNull();
        assertThat(listOfItemLevels.size()).isEqualTo(itemLevels.size());
    }

    @Test
    void findByNamePresent() {

        given(itemLevelRepository.findById(anyLong())).willReturn(Optional.of(itemLevel));

        itemLevel = itemLevelService.findById(1L);

        assertThat(itemLevel.getItemLevelId()).isEqualTo(itemLevel.getItemLevelId());
        assertThat(itemLevel.getLevel()).isEqualTo(itemLevel.getLevel());
        then(itemLevelRepository).should(times(1)).findById(1L);

    }

    @Test
    void findByNameNotPresent(){

        given(itemLevelRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(ItemLevelNotFoundException.class, () -> itemLevelService.findById(1L));
        then(itemLevelRepository).should(times(1)).findById(1L);

    }

    @Test
    void saveOrUpdateItemLevel() {

        itemLevel = new ItemLevel();
        itemLevel.setItemLevelId(1L);
        itemLevel.setLevel(ItemLevels.GOLD);

        given(itemLevelRepository.save(any(ItemLevel.class))).willReturn(itemLevel);

        itemLevel = itemLevelService.saveOrUpdateItemLevel(itemLevel);

        assertThat(itemLevel.getItemLevelId()).isEqualTo(itemLevel.getItemLevelId());
        assertThat(itemLevel.getLevel()).isEqualTo(itemLevel.getLevel());

    }

    @Test
    void deleteByNamePresent() {

        given(itemLevelRepository.findById(anyLong())).willReturn(Optional.of(itemLevel));

        itemLevelService.deleteById(1L);

        then(itemLevelRepository).should(times(1)).findById(1L);
        then(itemLevelRepository).should(times(1)).delete(itemLevel);
    }

    @Test
    void deleteByNameNotPresent() {

        given(itemLevelRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(ItemLevelNotFoundException.class, () -> itemLevelService.deleteById(1L));

    }

    @Test
    void getItemsItemLevelPresent() {
        Item item = new Item();
        item.setItemId(1L);

        items = new ArrayList<>();

        items.add(item);
        items.add(new Item());
        items.add(new Item());
        items.add(new Item());

        itemLevel.setItems(items);

        given(itemLevelRepository.findById(anyLong())).willReturn(Optional.of(itemLevel));

        List<Item> listOfItems = itemLevelService.getItems(1L);

        assertThat(listOfItems).isNotNull();
        assertThat(listOfItems.size()).isEqualTo(items.size());
    }

    @Test
    void getItemsItemLevelNotPresent() {
        given(itemLevelRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(ItemLevelNotFoundException.class, () -> itemLevelService.getItems(anyLong()));
        then(itemLevelRepository).should(times(1)).findById(anyLong());
    }

    @Test
    void getItemsItemLevelPresentItemsEmpty() {
        given(itemLevelRepository.findById(anyLong())).willReturn(Optional.of(itemLevel));

        assertThrows(EmptyItemsListException.class, () -> itemLevelService.getItems(anyLong()));
        then(itemLevelRepository).should(times(1)).findById(anyLong());
    }

    @Test
    void numberOfItemsItemLevelPresent() {

        Item item = new Item();
        item.setItemId(1L);

        items = new ArrayList<>();

        items.add(item);
        items.add(new Item());
        items.add(new Item());
        items.add(new Item());

        itemLevel.setItems(items);

        given(itemLevelRepository.findById(anyLong())).willReturn(Optional.of(itemLevel));

        Long levelCount = itemLevelService.numberOfItems(1L);

        assertThat(levelCount).isNotNull();
        assertThat(levelCount).isEqualTo(4L);

    }

    @Test
    void numberOfItemsItemLevelNotPresent() {

        given(itemLevelRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(ItemLevelNotFoundException.class, () -> itemLevelService.numberOfItems(anyLong()));
        then(itemLevelRepository).should(times(1)).findById(anyLong());

    }

    @Test
    void numberOfItemsItemLevelPresentItemsNotPresent() {
        given(itemLevelRepository.findById(anyLong())).willReturn(Optional.of(itemLevel));

        Long levelCount = itemLevelService.numberOfItems(1L);

        assertThat(levelCount).isNotNull();
        assertThat(levelCount).isEqualTo(0L);
    }
}