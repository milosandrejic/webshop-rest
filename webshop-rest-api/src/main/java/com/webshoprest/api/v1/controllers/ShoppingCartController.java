package com.webshoprest.api.v1.controllers;

import com.webshoprest.api.v1.services.ShoppingCartService;
import com.webshoprest.api.v1.util.SecurityUtility;
import com.webshoprest.domain.ShoppingCart;
import com.webshoprest.domain.ShoppingCartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static com.webshoprest.api.v1.security.SecurityConstants.CUSTOMER_AUTHORITY_STRINGS;

@PreAuthorize(CUSTOMER_AUTHORITY_STRINGS)
@RestController
@RequestMapping("/api/v1/users/{userId}/shopping-cart")
public class ShoppingCartController {

    private ShoppingCartService shoppingCartService;
    private SecurityUtility securityUtility;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService, SecurityUtility securityUtility) {
        this.shoppingCartService = shoppingCartService;
        this.securityUtility = securityUtility;
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ShoppingCart getShoppingCart(@PathVariable Long userId, Principal principal){
        securityUtility.checkIdentity(principal, userId);
        return shoppingCartService.findByUser(userId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/init")
    public ShoppingCart initShoppingCart(@PathVariable Long userId, Principal principal){
        securityUtility.checkIdentity(principal, userId);
        return shoppingCartService.initUserShoppingCart(userId);
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{shoppingCartId}/cart-items")
    public List<ShoppingCartItem> getShoppingCartItems(@PathVariable Long userId, @PathVariable Long shoppingCartId,
                                                       Principal principal){
        securityUtility.checkIdentity(principal, userId);
        return shoppingCartService.getShoppingCartItems(shoppingCartId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{shoppingCartId}/cart-items")
    public ShoppingCart addItemToShoppingCart(@RequestParam ("itemId") Long itemId,
                                                         @RequestParam("qty") Long orderedQty,
                                                         @PathVariable Long userId,
                                                         @PathVariable Long shoppingCartId,
                                              Principal principal){
        securityUtility.checkIdentity(principal, userId);
        return shoppingCartService.addItemToShoppingCart(userId, shoppingCartId, itemId, orderedQty);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{shoppingCartId}/cart-items/{shoppingCartItemId}")
    public ShoppingCart deleteItemFromShoppingCart(@PathVariable Long userId,
                                                         @PathVariable Long shoppingCartId,
                                                         @PathVariable Long shoppingCartItemId,
                                                   Principal principal){
        securityUtility.checkIdentity(principal, userId);
        return shoppingCartService.deleteItemFromShoppingCart(userId, shoppingCartId, shoppingCartItemId);
    }

}
