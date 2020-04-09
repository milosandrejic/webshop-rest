package com.webshoprest.api.v1.services;

import com.webshoprest.api.v1.exceptions.ItemNotFoundException;
import com.webshoprest.api.v1.services.impl.ItemServiceImpl;
import com.webshoprest.domain.*;
import com.webshoprest.domain.enums.ItemLevels;
import com.webshoprest.repositories.ItemRepository;
import com.webshoprest.repositories.OrderedItemRepository;
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

class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private OrderedItemRepository orderedItemRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    private Item item;

    private List<Item> items;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        item = new Item();
        item.setItemId(1L);
    }

    @Test
    void findAllItems() {

        items = new ArrayList<>();
        items.add(item);

        given(itemRepository.findAll()).willReturn(items);

        List<Item> itemsList = itemService.findAllItems();

        assertThat(itemsList).isNotNull();
        assertThat(itemsList.size()).isEqualTo(items.size());
        assertThat(itemsList.get(0).getItemId()).isEqualTo(item.getItemId());
        then(itemRepository).should(times(1)).findAll();

    }

    @Test
    void findByIdPresent() {
        given(itemRepository.findById(anyLong())).willReturn(Optional.of(item));

        Item foundItem = itemService.findById(1L);

        assertThat(foundItem).isNotNull();
        assertThat(foundItem.getItemId()).isEqualTo(item.getItemId());
        then(itemRepository).should(times(1)).findById(1L);
    }

    @Test
    void findByIdNotPresent(){
        given(itemRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> itemService.findById(1L));
        then(itemRepository).should(times(1)).findById(1L);
    }

    @Test
    void saveOrUpdateItem() {

        given(itemRepository.existsById(anyLong())).willReturn(true);
        given(itemRepository.save(any(Item.class))).willReturn(item);

        Item savedItem = itemService.saveOrUpdateItem(item);

        assertThat(savedItem).isNotNull();
        assertThat(savedItem.getItemId()).isEqualTo(item.getItemId());
        then(itemRepository).should(times(1)).save(any(Item.class));

    }

    @Test
    void saveOrUpdateItem_itemNotFound() {

        given(itemRepository.save(any(Item.class))).willReturn(item);
        given(itemRepository.existsById(anyLong())).willReturn(false);

        assertThrows(ItemNotFoundException.class, () -> itemService.saveOrUpdateItem(item));
    }

    @Test
    void deleteItemByIdPresent() {

        given(itemRepository.findById(anyLong())).willReturn(Optional.of(item));

        itemService.deleteItemById(1L);

        then(itemRepository).should(times(1)).findById(anyLong());
        then(itemRepository).should(times(1)).delete(item);
    }

    @Test
    void deleteItemByIdNotPresent() {

        given(itemRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> itemService.deleteItemById(anyLong()));

    }

    @Test
    void getCategory() {

        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setItemCategoryName("beers");

        item.setItemCategory(itemCategory);

        given(itemRepository.findById(anyLong())).willReturn(Optional.of(item));

        ItemCategory itemCategory1 = itemService.getCategory(1L);

        assertThat(itemCategory1).isNotNull();
        assertThat(itemCategory1.getItemCategoryName()).isEqualTo(item.getItemCategory().getItemCategoryName());
        then(itemRepository).should(times(1)).findById(1L);

    }

    @Test
    void getCategoryItemNotPresent() {

        given(itemRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> itemService.getCategory(1L));
        then(itemRepository).should(times(1)).findById(anyLong());

    }


    @Test
    void getLevel() {

        ItemLevel itemLevel = new ItemLevel();
        itemLevel.setLevel(ItemLevels.GOLD);

        item.setItemLevel(itemLevel);

        given(itemRepository.findById(anyLong())).willReturn(Optional.of(item));

        ItemLevel itemLevel1 = itemService.getLevel(1L);

        assertThat(itemLevel1).isNotNull();
        assertThat(itemLevel1.getLevel()).isEqualTo(item.getItemLevel().getLevel());
        then(itemRepository).should(times(1)).findById(1L);

    }

    @Test
    void getLevelItemNotPresent() {
        given(itemRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> itemService.getLevel(anyLong()));
        then(itemRepository).should(times(1)).findById(anyLong());
    }


    @Test
    void topSellingItems() {

        OrderedItem orderedItem1 = new OrderedItem();
        orderedItem1.setOrderedItemId(1L);
        orderedItem1.setOrderedItemName("name");

        OrderedItem orderedItem2 = new OrderedItem();
        orderedItem2.setOrderedItemId(2L);
        orderedItem2.setOrderedItemName("name2");

        OrderedItem orderedItem3 = new OrderedItem();
        orderedItem3.setOrderedItemId(3L);
        orderedItem3.setOrderedItemName("name1");

        List<OrderItemOrder> list = new ArrayList<>();
        list.add(new OrderItemOrder());
        list.add(new OrderItemOrder());
        list.add(new OrderItemOrder());

        orderedItem1.setOrderItemOrders(list);
        orderedItem2.setOrderItemOrders(list);
        orderedItem3.setOrderItemOrders(list);

        given(orderedItemRepository.findAll()).willReturn(List.of(orderedItem1, orderedItem2, orderedItem3));

        List<OrderedItem> orderedItems = itemService.topSellingItems(2L);

        assertThat(orderedItems.size()).isEqualTo(2);
    }

}