package com.webshoprest.api.v1.services;


import com.webshoprest.domain.ShoppingCart;
import com.webshoprest.domain.ShoppingCartItem;

import java.util.List;

public interface ShoppingCartService {

    ShoppingCart findByUser(Long userId);

    ShoppingCart initUserShoppingCart(Long userId);

    List<ShoppingCartItem> getShoppingCartItems(Long shoppingCartId, Long userId);

    ShoppingCart addItemToShoppingCart(Long userId, Long shoppingCartId, Long itemId, Long orderedQty);

    ShoppingCart deleteItemFromShoppingCart(Long userId, Long shoppingCartId, Long itemId);

}
