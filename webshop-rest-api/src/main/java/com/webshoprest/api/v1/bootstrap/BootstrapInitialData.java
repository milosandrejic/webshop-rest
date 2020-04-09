package com.webshoprest.api.v1.bootstrap;

import com.webshoprest.domain.*;
import com.webshoprest.domain.enums.ItemLevels;
import com.webshoprest.domain.enums.UnitOfMeasure;
import com.webshoprest.repositories.ItemLevelRepository;
import com.webshoprest.repositories.ItemRepository;
import com.webshoprest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class BootstrapInitialData implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemLevelRepository itemLevelRepository;

    @Override
    public void run(String... args) throws Exception {

        User user = new User();
        user.setUserId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername("username");
        user.setPassword("password");
        user.setDob(LocalDate.of(1990, 2,12));
        user.setEmail("johndoe@gmail.com");
        user.setPhoneNumber("1234521312");

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setTotalAmount(20.00);

        user.setShoppingCart(shoppingCart);

        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        shoppingCartItem.setOrderedQty(50L);

        Item item = new Item();
        item.setItemName("name");
        item.setDiscount(0.0);
        item.setPrice(20.00);
        item.setQty(10L);
        item.setDescription("desc");
        item.setUnitOfMeasure(UnitOfMeasure.PIECE);

        shoppingCartItem.setItem(item);

        List<ShoppingCartItem> cartItems = new ArrayList<>();
        cartItems.add(shoppingCartItem);

        shoppingCart.setShoppingCartItems(cartItems);

        Country country = new Country();
        country.setCountryName("Serbia");

        City city = new City();
        city.setCityName("Belgrade");
        city.setCountry(country);

        Address address = new Address();
        address.setStreet("Street");
        address.setStreetNumber(15L);
        address.setCity(city);

        user.setAddress(address);

        ItemLevel itemLevel1 = new ItemLevel();
        itemLevel1.setLevel(ItemLevels.GOLD);
        itemLevelRepository.save(itemLevel1);

        ItemLevel itemLevel2 = new ItemLevel();
        itemLevel2.setLevel(ItemLevels.SILVER);
        itemLevelRepository.save(itemLevel2);

        ItemLevel itemLevel3 = new ItemLevel();
        itemLevel3.setLevel(ItemLevels.STANDARD);
        itemLevelRepository.save(itemLevel3);

        itemRepository.save(item);
        userRepository.save(user);

    }
}
