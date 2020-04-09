package com.webshoprest.repositories;

import com.webshoprest.domain.ShoppingCartItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ShoppingCartItemRepositoryTest {

    @Autowired
    private ShoppingCartItemRepository shoppingCartItemRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void testSaveNewItem() {
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        assertThat(shoppingCartItem.getShoppingCartItemId()).isNull();
        shoppingCartItem = shoppingCartItemRepository.save(shoppingCartItem);
        assertThat(shoppingCartItem.getShoppingCartItemId()).isNotNull();
    }

    @Test
    void testUpdateItem() {
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        shoppingCartItem.setOrderedQty(50L);
        shoppingCartItem = shoppingCartItemRepository.save(shoppingCartItem);

        Long id = shoppingCartItem.getShoppingCartItemId();

        shoppingCartItem.setOrderedQty(20L);
        shoppingCartItem = shoppingCartItemRepository.save(shoppingCartItem);

        assertThat(shoppingCartItem.getShoppingCartItemId()).isEqualTo(id);
        assertThat(shoppingCartItem.getOrderedQty()).isEqualTo(20L);
    }

    @Test
    void testDeleteById() {
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        shoppingCartItem = shoppingCartItemRepository.save(shoppingCartItem);

        shoppingCartItemRepository.deleteById(shoppingCartItem.getShoppingCartItemId());
        assertThat(shoppingCartItemRepository.existsById(shoppingCartItem.getShoppingCartItemId())).isFalse();
    }

    @Test
    void testExistsById_exists() {
        shoppingCartItemRepository.save(new ShoppingCartItem());
        boolean result = shoppingCartItemRepository.existsById(1L);
        assertThat(result).isTrue();
    }

    @Test
    void testExistsById_dontExists() {

        boolean result = shoppingCartItemRepository.existsById(222L);
        assertThat(result).isFalse();
    }
}