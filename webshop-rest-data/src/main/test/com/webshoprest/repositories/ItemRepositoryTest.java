package com.webshoprest.repositories;

import com.webshoprest.domain.Item;
import com.webshoprest.domain.enums.UnitOfMeasure;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @BeforeAll
    private void initData() {
        Item item1 = new Item();
        item1.setItemName("item1");
        item1.setDescription("desc");
        item1.setQty(50L);
        item1.setPrice(25.00);
        item1.setUnitOfMeasure(UnitOfMeasure.PIECE);

        Item item2 = new Item();
        item2.setItemName("item2");
        item2.setDescription("desc");
        item2.setQty(20L);
        item2.setPrice(45.00);
        item2.setUnitOfMeasure(UnitOfMeasure.PIECE);

        Item item3 = new Item();
        item3.setItemName("item3");
        item3.setDescription("desc");
        item3.setQty(30L);
        item3.setPrice(15.00);
        item3.setUnitOfMeasure(UnitOfMeasure.PIECE);

        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        items.add(item3);

        itemRepository.saveAll(items);
    }

    @Test
    void testFindById() {
        Optional<Item> item = itemRepository.findById(1L);
        assertThat(item.isPresent()).isTrue();
        assertThat(item.get().getItemId()).isEqualTo(1L);
    }

    @Test
    void testFindAll() {
        List<Item> items = itemRepository.findAll();
        assertThat(items.size()).isEqualTo(3);
    }

    @Test
    void testUpdateItem() {
        Optional<Item> item = itemRepository.findById(1L);

        item.get().setQty(90L);
        Item updatedItem = itemRepository.save(item.get());

        assertThat(updatedItem.getItemId()).isEqualTo(1L);
        assertThat(updatedItem.getQty()).isEqualTo(90L);
    }

    @Test
    void testSaveNewItem() {
        Item item = new Item();
        item.setItemName("item4");
        item.setDescription("desc");
        item.setUnitOfMeasure(UnitOfMeasure.PIECE);
        item.setQty(100L);
        item.setPrice(20.00);

        assertThat(item.getItemId()).isNull();

        item = itemRepository.save(item);

        assertThat(item.getItemId()).isNotNull();
        assertThat(item.getQty()).isEqualTo(100L);
        assertThat(itemRepository.count()).isEqualTo(4);
    }

    @Test
    void testExistsById_true() {
        assertThat(itemRepository.existsById(1L)).isTrue();
    }

    @Test
    void testExistById_false() {
        assertThat(itemRepository.existsById(100L)).isFalse();
    }

    @Test
    void testDeleteById() {
        itemRepository.deleteById(1L);

        Optional<Item> item = itemRepository.findById(1L);

        assertThat(item.isPresent()).isFalse();
        assertThat(itemRepository.count()).isEqualTo(2);
    }

    @Test
    void testDeleteByObject() {
        Optional<Item> item = itemRepository.findById(1L);
        itemRepository.delete(item.get());
        Optional<Item> deletedItem = itemRepository.findById(1L);
        assertThat(deletedItem.isPresent()).isFalse();
        assertThat(itemRepository.count()).isEqualTo(2);
    }

    @Test
    void testDeleteAll(){
        itemRepository.deleteAll();
        assertThat(itemRepository.count()).isZero();
    }
}