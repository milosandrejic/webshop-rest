package com.webshoprest.repositories;

import com.webshoprest.domain.ItemLevel;
import com.webshoprest.domain.enums.ItemLevels;
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
class ItemLevelRepositoryTest {

    @Autowired
    private ItemLevelRepository itemLevelRepository;

    @BeforeAll
    private void initData() {
        ItemLevel itemLevel1 = new ItemLevel();
        itemLevel1.setLevel(ItemLevels.GOLD);

        ItemLevel itemLevel2 = new ItemLevel();
        itemLevel1.setLevel(ItemLevels.SILVER);

        ItemLevel itemLevel3 = new ItemLevel();
        itemLevel1.setLevel(ItemLevels.STANDARD);

        List<ItemLevel> itemLevels = new ArrayList<>();
        itemLevels.add(itemLevel1);
        itemLevels.add(itemLevel2);
        itemLevels.add(itemLevel3);

        itemLevelRepository.saveAll(itemLevels);
    }

    @Test
    void testFindById() {
        Optional<ItemLevel> itemLevel = itemLevelRepository.findById(1L);
        assertThat(itemLevel.isPresent()).isTrue();
        assertThat(itemLevel.get().getItemLevelId()).isEqualTo(1);
    }

    @Test
    void testFindAll() {
        List<ItemLevel> itemLevels = itemLevelRepository.findAll();
        assertThat(itemLevels.size()).isEqualTo(3);
    }

    @Test
    void testFindByLevel() {
        Optional<ItemLevel> itemLevel = itemLevelRepository.findByLevel(ItemLevels.STANDARD);
        assertThat(itemLevel.isPresent()).isTrue();
        assertThat(itemLevel.get().getLevel()).isEqualTo(ItemLevels.STANDARD);
    }


    @Test
    void testDeleteById() {
        itemLevelRepository.deleteById(1L);
        assertThat(itemLevelRepository.count()).isEqualTo(2);
    }

    @Test
    void testExistById_true() {
        assertThat(itemLevelRepository.existsById(1L)).isTrue();
    }

    @Test
    void textExistById_false() {
        assertThat(itemLevelRepository.existsById(55L)).isFalse();
    }

    @Test
    void testDeleteAll() {
        itemLevelRepository.deleteAll();
        assertThat(itemLevelRepository.count()).isZero();
    }

}