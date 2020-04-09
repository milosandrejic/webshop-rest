package com.webshoprest.repositories;

import com.webshoprest.domain.ShoppingCart;
import com.webshoprest.domain.ShoppingCartItem;
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
class ShoppingCartRepositoryTest {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ShoppingCartItemRepository shoppingCartItemRepository;

    @BeforeAll
    private void initData(){
        ShoppingCart shoppingCart = new ShoppingCart();

        ShoppingCartItem shoppingCartItem1 = new ShoppingCartItem();
        ShoppingCartItem shoppingCartItem2= new ShoppingCartItem();
        ShoppingCartItem shoppingCartItem3 = new ShoppingCartItem();
        ShoppingCartItem shoppingCartItem4 = new ShoppingCartItem();
        ShoppingCartItem shoppingCartItem5 = new ShoppingCartItem();

        List<ShoppingCartItem> shoppingCartItems = new ArrayList<>();
        shoppingCartItems.add(shoppingCartItem1);
        shoppingCartItems.add(shoppingCartItem2);
        shoppingCartItems.add(shoppingCartItem3);
        shoppingCartItems.add(shoppingCartItem4);
        shoppingCartItems.add(shoppingCartItem5);

        shoppingCart.setShoppingCartItems(shoppingCartItems);

        shoppingCartRepository.save(shoppingCart);
    }

    @Test
    void testFindById() {
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findById(1L);
        assertThat(shoppingCart.isPresent()).isTrue();
    }

    @Test
    void testRemoveOrphanRemovalShoppingCartItem() {
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findById(1L);
        ShoppingCart cart = shoppingCart.get();

        assertThat(shoppingCartItemRepository.count()).isEqualTo(5L);

        cart.getShoppingCartItems().removeIf(e -> e.getShoppingCartItemId().equals(1L));
        shoppingCartRepository.save(cart);

        assertThat(shoppingCartItemRepository.count()).isEqualTo(4L);
    }
}