package com.webshoprest.api.v1.services;

import com.webshoprest.api.v1.exceptions.*;
import com.webshoprest.api.v1.services.impl.ShoppingCartServiceImpl;
import com.webshoprest.domain.Item;
import com.webshoprest.domain.ShoppingCart;
import com.webshoprest.domain.ShoppingCartItem;
import com.webshoprest.domain.User;
import com.webshoprest.repositories.ItemRepository;
import com.webshoprest.repositories.ShoppingCartItemRepository;
import com.webshoprest.repositories.ShoppingCartRepository;
import com.webshoprest.repositories.UserRepository;
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

class ShoppingCartServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ShoppingCartItemRepository shoppingCartItemRepository;
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    private User user;
    private ShoppingCart shoppingCart;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        user = new User();
        user.setUserId(1L);

        shoppingCart = new ShoppingCart();
        shoppingCart.setShoppingCartId(2L);
    }

    @Test
    void findByUser() {
        user.setShoppingCart(shoppingCart);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        ShoppingCart foundShoppingCart = shoppingCartService.findByUser(1L);

        assertThat(foundShoppingCart.getShoppingCartId()).isEqualTo(user.getShoppingCart().getShoppingCartId());
        then(userRepository).should(times(1)).findById(1L);
    }

    @Test
    void findByUser_NotInitializedCart(){
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        assertThrows(ShoppingCartNotInitializedException.class, () -> shoppingCartService.findByUser(1L));
        then(userRepository).should(times(1)).findById(1L);
    }

    @Test
    void findByUser_UserNotFound(){
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> shoppingCartService.findByUser(1L));
        then(userRepository).should(times(1)).findById(1L);
    }

    @Test
    void initUserShoppingCart() {
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(userRepository.save(any(User.class))).willReturn(user);

        ShoppingCart initializedCart = shoppingCartService.initUserShoppingCart(1L);

        assertThat(initializedCart).isNotNull();
        then(userRepository).should(times(1)).findById(1L);
        then(userRepository).should(times(1)).save(any(User.class));
    }

    @Test
    void initUserShoppingCart_userNotFound(){
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> shoppingCartService.findByUser(anyLong()));
    }

    @Test
    void getShoppingCartItems() {
        List<ShoppingCartItem> items = new ArrayList<>();
        items.add(new ShoppingCartItem());
        items.add(new ShoppingCartItem());
        items.add(new ShoppingCartItem());

        shoppingCart.setShoppingCartItems(items);
        user.setShoppingCart(shoppingCart);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        List<ShoppingCartItem> cartItems = shoppingCartService.getShoppingCartItems(2L , 1L);

        assertThat(cartItems.size()).isEqualTo(items.size());
        then(userRepository).should(times(1)).findById(1L);
    }

    @Test
    void getShoppingCartItems_NotInitialized(){
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        assertThrows(ShoppingCartNotInitializedException.class, () -> shoppingCartService.getShoppingCartItems(2L, 1L));
        then(userRepository).should(times(1)).findById(anyLong());
    }

    @Test
    void getShoppingCartItems_DontBelongToUser(){
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setShoppingCartId(5L);
        user.setShoppingCart(shoppingCart);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        assertThrows(ShoppingCartDontBelongToUserException.class, () -> shoppingCartService.getShoppingCartItems(2L, 1L));
        then(userRepository).should(times(1)).findById(anyLong());
    }

    @Test
    void getShoppingCartItems_UserNotFound(){
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> shoppingCartService.getShoppingCartItems(2L, 1L));
        then(userRepository).should(times(1)).findById(anyLong());
    }

    @Test
    void addItemToShoppingCart() {
        List<ShoppingCartItem> cartItems = new ArrayList<>();

        Item item = new Item();
        item.setItemId(2L);
        item.setPrice(20.00);
        item.setQty(100L);

        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        shoppingCartItem.setShoppingCart(shoppingCart);
        shoppingCartItem.setItem(item);
        shoppingCartItem.setTotalItemPrice(20.00);

        cartItems.add(shoppingCartItem);

        shoppingCart.setShoppingCartItems(cartItems);

        user.setShoppingCart(shoppingCart);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(shoppingCartRepository.save(any(ShoppingCart.class))).willReturn(shoppingCart);
        given(itemRepository.findById(anyLong())).willReturn(Optional.of(item));

        ShoppingCart cart = shoppingCartService.addItemToShoppingCart(1L, 2L, 1L, 50L);

        assertThat(cart.getShoppingCartItems().size()).isEqualTo(2);
        then(shoppingCartRepository).should(times(1)).save(any(ShoppingCart.class));
    }

    @Test
    void addItemToShoppingCart_throwsOrderedQuantityException() {
        List<ShoppingCartItem> cartItems = new ArrayList<>();
        cartItems.add(new ShoppingCartItem());
        cartItems.add(new ShoppingCartItem());
        cartItems.add(new ShoppingCartItem());

        shoppingCart.setShoppingCartItems(cartItems);

        user.setShoppingCart(shoppingCart);

        Item item = new Item();
        item.setPrice(20.00);
        item.setQty(30L);

        given(itemRepository.findById(anyLong())).willReturn(Optional.of(item));
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(shoppingCartRepository.save(any(ShoppingCart.class))).willReturn(shoppingCart);


        assertThrows(OrderedQuantityException.class, () -> shoppingCartService.addItemToShoppingCart(1L, 2L, 1L, 50L));
    }

    @Test
    void addItemToShoppingCart_CartDontBelongToUser(){
        user.setShoppingCart(shoppingCart);

        Item item = new Item();
        item.setPrice(20.00);
        item.setQty(30L);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(itemRepository.findById(anyLong())).willReturn(Optional.of(item));

        assertThrows(ShoppingCartDontBelongToUserException.class, () -> shoppingCartService.addItemToShoppingCart(1L, 22L, 1L, 50L));
        then(userRepository).should(times(1)).findById(1L);
    }

    @Test
    void addItemToShoppingCart_ItemAlreadyAdded(){
        List<ShoppingCartItem> cartItems = new ArrayList<>();

        Item item = new Item();
        item.setItemId(1L);
        item.setPrice(20.00);
        item.setQty(100L);

        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        shoppingCartItem.setShoppingCart(shoppingCart);
        shoppingCartItem.setItem(item);

        cartItems.add(shoppingCartItem);

        shoppingCart.setShoppingCartItems(cartItems);

        user.setShoppingCart(shoppingCart);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(shoppingCartRepository.save(any(ShoppingCart.class))).willReturn(shoppingCart);
        given(itemRepository.findById(anyLong())).willReturn(Optional.of(item));

        assertThrows(ItemAlreadyAddToShoppingCartException.class, () -> shoppingCartService.addItemToShoppingCart(1L, 2L, 1L, 50L));
    }

    @Test
    void addItemToShoppingCart_CartNotInitialized(){
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        assertThrows(ShoppingCartNotInitializedException.class, () -> shoppingCartService.addItemToShoppingCart(1L, 2L, 1L, 50L));
        then(userRepository).should(times(1)).findById(1L);
    }

    @Test
    void addItemToShoppingCart_itemNotFound(){
        user.setShoppingCart(shoppingCart);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(itemRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> shoppingCartService.addItemToShoppingCart(1L, 2L, 1L, 50L));
        then(userRepository).should(times(1)).findById(1L);
    }

    @Test
    void addItemToShoppingCart_UserNotFound(){
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> shoppingCartService.addItemToShoppingCart(1L, 2L, 1L, 50L));
        then(userRepository).should(times(1)).findById(1L);
    }

    @Test
    void deleteItemFromShoppingCart() {
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        shoppingCartItem.setShoppingCartItemId(5L);
        shoppingCartItem.setTotalItemPrice(20.00);

        ShoppingCartItem shoppingCartItem2 = new ShoppingCartItem();
        shoppingCartItem2.setShoppingCartItemId(4L);
        shoppingCartItem2.setTotalItemPrice(20.00);

        List<ShoppingCartItem> cartItems = new ArrayList<>();
        cartItems.add(shoppingCartItem);
        cartItems.add(shoppingCartItem2);

        shoppingCart.setShoppingCartItems(cartItems);

        user.setShoppingCart(shoppingCart);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(shoppingCartRepository.save(any(ShoppingCart.class))).willReturn(shoppingCart);
        given(shoppingCartItemRepository.existsById(anyLong())).willReturn(true);

        ShoppingCart cart = shoppingCartService.deleteItemFromShoppingCart(1L, 2L, 5L);

        assertThat(cart.getShoppingCartItems().size()).isEqualTo(1);
        assertThat(cart.getTotalAmount()).isEqualTo(20.00);
        then(userRepository).should(times(1)).findById(1L);
        then(shoppingCartRepository).should(times(1)).save(any(ShoppingCart.class));
        then(shoppingCartItemRepository).should(times(1)).existsById(5L);
    }

    @Test
    void deleteItemFromShoppingCart_UserNotFound() {
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> shoppingCartService.deleteItemFromShoppingCart(1L, 2L, 3L));
        then(userRepository).should(times(1)).findById(1L);
    }

    @Test
    void deleteItemFromShoppingCart_CartDontBelongToUser() {
        user.setShoppingCart(shoppingCart);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        assertThrows(ShoppingCartDontBelongToUserException.class, () -> shoppingCartService.deleteItemFromShoppingCart(1L, 5L, 5L));
        then(userRepository).should(times(1)).findById(1L);
    }

    @Test
    void deleteItemFromShoppingCart_ItemNotFound() {
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        shoppingCartItem.setShoppingCartItemId(5L);

        ShoppingCartItem shoppingCartItem2 = new ShoppingCartItem();
        shoppingCartItem2.setShoppingCartItemId(4L);

        List<ShoppingCartItem> cartItems = new ArrayList<>();
        cartItems.add(shoppingCartItem);
        cartItems.add(shoppingCartItem2);

        shoppingCart.setShoppingCartItems(cartItems);

        user.setShoppingCart(shoppingCart);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(shoppingCartItemRepository.existsById(anyLong())).willReturn(false);

        assertThrows(ItemNotFoundException.class, () -> shoppingCartService.deleteItemFromShoppingCart(1L, 2L, 10L));
        then(userRepository).should(times(1)).findById(1L);
    }

    @Test
    void deleteItemFromShoppingCart_NotInitializedCart(){
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        assertThrows(ShoppingCartNotInitializedException.class, () -> shoppingCartService.deleteItemFromShoppingCart(1L, 2L, 3L));
        then(userRepository).should(times(1)).findById(1L);
    }
}