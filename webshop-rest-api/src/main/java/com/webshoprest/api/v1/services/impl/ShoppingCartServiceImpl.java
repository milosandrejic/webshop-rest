package com.webshoprest.api.v1.services.impl;

import com.webshoprest.api.v1.exceptions.*;
import com.webshoprest.api.v1.services.ShoppingCartService;
import com.webshoprest.domain.Item;
import com.webshoprest.domain.ShoppingCart;
import com.webshoprest.domain.ShoppingCartItem;
import com.webshoprest.domain.User;
import com.webshoprest.repositories.ItemRepository;
import com.webshoprest.repositories.ShoppingCartItemRepository;
import com.webshoprest.repositories.ShoppingCartRepository;
import com.webshoprest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private UserRepository userRepository;
    private ShoppingCartRepository shoppingCartRepository;
    private ShoppingCartItemRepository shoppingCartItemRepository;
    private ItemRepository itemRepository;

    @Autowired
    public ShoppingCartServiceImpl(UserRepository userRepository, ShoppingCartRepository shoppingCartRepository, ShoppingCartItemRepository shoppingCartItemRepository, ItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartItemRepository = shoppingCartItemRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public ShoppingCart findByUser(Long userId) {

        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            if (user.get().getShoppingCart() != null) {
                return user.get().getShoppingCart();
            } else {
                throw new ShoppingCartNotInitializedException();
            }

        }

        throw new UserNotFoundException();

    }

    @Override
    public ShoppingCart initUserShoppingCart(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty()){
            throw new UserNotFoundException();
        }

        if (user.get().getShoppingCart() == null) {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setTotalAmount(0.0);
            User tempUser = user.get();
            tempUser.setShoppingCart(shoppingCart);
            tempUser = userRepository.save(tempUser);
            return tempUser.getShoppingCart();
        }
        return user.get().getShoppingCart();
    }

    @Override
    public List<ShoppingCartItem> getShoppingCartItems(Long shoppingCartId, Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            try {
                if (user.get().getShoppingCart().getShoppingCartId().equals(shoppingCartId)) {
                    return user.get().getShoppingCart().getShoppingCartItems();
                } else {
                    throw new ShoppingCartDontBelongToUserException();
                }
            } catch (NullPointerException e) {
                throw new ShoppingCartNotInitializedException();
            }
        }

        throw new UserNotFoundException();
    }

    @Override
    public ShoppingCart addItemToShoppingCart(Long userId, Long shoppingCartId, Long itemId, Long orderedQty) {

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {

            User user = optionalUser.get();

            if (user.getShoppingCart() == null) {
                throw new ShoppingCartNotInitializedException();
            }

            Optional<Item> tempItem = itemRepository.findById(itemId);
            Item item;

            if(tempItem.isPresent()){
                item = tempItem.get();
            } else {
                throw new ItemNotFoundException();
            }

            if (user.getShoppingCart().getShoppingCartId().equals(shoppingCartId)) {

                if(orderedQty > item.getQty()){
                    throw new OrderedQuantityException();
                }

                boolean alreadyAdded = user.getShoppingCart().getShoppingCartItems()
                        .stream()
                        .anyMatch(e -> e.getItem().getItemId().equals(itemId));

                if(alreadyAdded){
                    throw new ItemAlreadyAddToShoppingCartException();
                }

                user.getShoppingCart()
                        .getShoppingCartItems().add(mapToCartItem(item, orderedQty));

                user.getShoppingCart()
                        .setTotalAmount(calculateTotalShoppingCartValue(user.getShoppingCart()));

                return shoppingCartRepository.save(user.getShoppingCart());
            } else {
                throw new ShoppingCartDontBelongToUserException();
            }

        }
        throw new UserNotFoundException();
    }

    @Override
    public ShoppingCart deleteItemFromShoppingCart(Long userId, Long shoppingCartId, Long itemId) {

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {

            User user = optionalUser.get();

            ShoppingCart shoppingCart;

            if (user.getShoppingCart() == null) {
                throw new ShoppingCartNotInitializedException();
            }

            shoppingCart = user.getShoppingCart();

            if (shoppingCart.getShoppingCartId().equals(shoppingCartId)) {
                if (shoppingCartItemRepository.existsById(itemId)) {
                    if(shoppingCart.getShoppingCartItems().removeIf(item -> item.getShoppingCartItemId().equals(itemId))){
                        shoppingCart.setTotalAmount(calculateTotalShoppingCartValue(shoppingCart));
                    }
                    return shoppingCartRepository.save(shoppingCart);
                }
                throw new ItemNotFoundException();
            }
            throw new ShoppingCartDontBelongToUserException();
        }

        throw new UserNotFoundException();

    }

    private ShoppingCartItem mapToCartItem(Item item, Long orderedQty) {
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        shoppingCartItem.setItem(item);
        shoppingCartItem.setOrderedQty(orderedQty);
        System.out.println("item: " + item.getPrice() + "\n qty=" + orderedQty);
        shoppingCartItem.setTotalItemPrice(item.getPrice() * orderedQty);
        return shoppingCartItem;
    }

    private double calculateTotalShoppingCartValue(ShoppingCart shoppingCart){
        return shoppingCart.getShoppingCartItems()
                .stream()
                .mapToDouble(ShoppingCartItem::getTotalItemPrice)
                .sum();
    }

}
