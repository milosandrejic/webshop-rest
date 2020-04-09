package com.webshoprest.api.v1.controllers;

import com.webshoprest.api.v1.services.ShoppingCartService;
import com.webshoprest.domain.ShoppingCart;
import com.webshoprest.domain.ShoppingCartItem;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userId}/shopping-cart")
public class ShoppingCartController {

    private ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ShoppingCart getShoppingCart(@PathVariable Long userId){
        return shoppingCartService.findByUser(userId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/init")
    public ShoppingCart initShoppingCart(@PathVariable Long userId){
        return shoppingCartService.initUserShoppingCart(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{shoppingCartId}/cart-items")
    public List<ShoppingCartItem> getShoppingCartItems(@PathVariable Long userId, @PathVariable Long shoppingCartId){
        return shoppingCartService.getShoppingCartItems(shoppingCartId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{shoppingCartId}/cart-items")
    public ShoppingCart addItemToShoppingCart(@RequestParam ("itemId") Long itemId,
                                                         @RequestParam("qty") Long orderedQty,
                                                         @PathVariable Long userId,
                                                         @PathVariable Long shoppingCartId){
        return shoppingCartService.addItemToShoppingCart(userId, shoppingCartId, itemId, orderedQty);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{shoppingCartId}/cart-items/{shoppingCartItemId}")
    public ShoppingCart deleteItemFromShoppingCart(@PathVariable Long userId,
                                                         @PathVariable Long shoppingCartId,
                                                         @PathVariable Long shoppingCartItemId){
        return shoppingCartService.deleteItemFromShoppingCart(userId, shoppingCartId, shoppingCartItemId);
    }

}
