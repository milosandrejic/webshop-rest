package com.webshoprest.repositories;

import com.webshoprest.domain.ItemCategory;
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
class ItemCategoryRepositoryTest {

    @Autowired
    private ItemCategoryRepository itemCategoryRepository;

    @BeforeAll
    private void initData(){
        ItemCategory itemCategory1 = new ItemCategory();
        itemCategory1.setItemCategoryName("Alcohol drinks");
        itemCategory1.setItemCategoryDescription("desc");
        ItemCategory itemCategory2 = new ItemCategory();
        itemCategory2.setItemCategoryName("DRY_BAKING_GOODS");
        itemCategory2.setItemCategoryDescription("desc");
        ItemCategory itemCategory3 = new ItemCategory();
        itemCategory3.setItemCategoryName("Beverages");
        itemCategory3.setItemCategoryDescription("desc");

        List<ItemCategory> categories = new ArrayList<>();
        categories.add(itemCategory1);
        categories.add(itemCategory2);
        categories.add(itemCategory3);

        itemCategoryRepository.saveAll(categories);
    }

    @Test
    void testFindById() {
        Optional<ItemCategory> itemCategory1 = itemCategoryRepository.findById(1L);
        assertThat(itemCategory1.isPresent()).isTrue();
        assertThat(itemCategory1.get().getItemCategoryId()).isEqualTo(1L);
    }

    @Test
    void testFindAll() {
        List<ItemCategory> itemCategories = itemCategoryRepository.findAll();
        assertThat(itemCategories.size()).isEqualTo(itemCategories.size());
        assertThat(itemCategories.get(0).getItemCategoryId()).isNotNull();
    }

    @Test
    void testFindByCategoryNameIgnoreCase_exactCase() {
        Optional<ItemCategory> itemCategory = itemCategoryRepository.findByItemCategoryNameIgnoreCase("Beverages");
        assertThat(itemCategory.isPresent()).isTrue();
        assertThat(itemCategory.get().getItemCategoryName()).isEqualTo("Beverages");
    }

    @Test
    void testFindByCategoryNameIgnoreCase_differentCase() {
        Optional<ItemCategory> itemCategory = itemCategoryRepository.findByItemCategoryNameIgnoreCase("beverages");
        assertThat(itemCategory.isPresent()).isTrue();
        assertThat(itemCategory.get().getItemCategoryName()).isEqualTo("Beverages");
    }

    @Test
    void testDeleteById() {
        itemCategoryRepository.deleteById(1L);
        assertThat(itemCategoryRepository.count()).isEqualTo(2);
    }

    @Test
    void testExistById(){
        assertThat(itemCategoryRepository.existsById(1L)).isTrue();
    }

    @Test
    void testDeleteAll() {
        itemCategoryRepository.deleteAll();
        assertThat(itemCategoryRepository.count()).isZero();
    }

    @Test
    void testExistsByItemCategoryName_exists_sameCase() {
        boolean result = itemCategoryRepository.existsByItemCategoryNameIgnoreCase("Beverages");
        assertThat(result).isTrue();
    }

    @Test
    void testExistsByItemCategoryName_exists_differentCase() {
        boolean result = itemCategoryRepository.existsByItemCategoryNameIgnoreCase("beverages");
        assertThat(result).isTrue();
    }

    @Test
    void testExistsByItemCategoryName_dontExists() {
        boolean result = itemCategoryRepository.existsByItemCategoryNameIgnoreCase("something");
        assertThat(result).isFalse();
    }
}