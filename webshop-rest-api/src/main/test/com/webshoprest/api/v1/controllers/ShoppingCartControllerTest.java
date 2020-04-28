package com.webshoprest.api.v1.controllers;

import com.google.gson.Gson;
import com.webshoprest.WebshopRestApplication;
import com.webshoprest.api.v1.config.SecurityConfig;
import com.webshoprest.api.v1.exceptions.ShoppingCartNotInitializedException;
import com.webshoprest.api.v1.exceptions.UserNotFoundException;
import com.webshoprest.api.v1.handlers.ShoppingCartExceptionHandler;
import com.webshoprest.api.v1.handlers.UserExceptionHandler;
import com.webshoprest.api.v1.security.GlobalAuthenticationEntryPoint;
import com.webshoprest.api.v1.security.JwtTokenProvider;
import com.webshoprest.api.v1.security.UserAuthenticationEntryPoint;
import com.webshoprest.api.v1.services.ShoppingCartService;
import com.webshoprest.api.v1.services.impl.UserSecurityService;
import com.webshoprest.api.v1.util.SecurityUtility;
import com.webshoprest.domain.Item;
import com.webshoprest.domain.ShoppingCart;
import com.webshoprest.domain.ShoppingCartItem;
import com.webshoprest.domain.enums.Roles;
import com.webshoprest.repositories.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static com.webshoprest.api.v1.TestConfig.initAuthentication;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {
        WebshopRestApplication.class,
        SecurityConfig.class,
        UserAuthenticationEntryPoint.class,
        GlobalAuthenticationEntryPoint.class,
        BCryptPasswordEncoder.class,
        JwtTokenProvider.class,
        UserSecurityService.class})
@AutoConfigureMockMvc
@WebMvcTest(ShoppingCartController.class)
class ShoppingCartControllerTest {

    private final String BASE_URL = "/api/v1/users/1/shopping-cart";

    @MockBean
    private ShoppingCartService shoppingCartService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ShoppingCartController shoppingCartController;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private SecurityUtility securityUtility;

    private ShoppingCart shoppingCart;

    Principal principal;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(shoppingCartController)
                .setControllerAdvice(UserExceptionHandler.class, ShoppingCartExceptionHandler.class)
                .build();

        shoppingCart = new ShoppingCart();
        shoppingCart.setShoppingCartId(1L);

        initAuthentication(Roles.CUSTOMER_GOLD);
    }

    @Test
    void testGetShoppingCart_cartExist() throws Exception {
        given(shoppingCartService.findByUser(anyLong())).willReturn(shoppingCart);

        mockMvc.perform(get(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shoppingCartId", equalTo(1)));
    }

    @SneakyThrows
    @Test
    public void testGetShoppingCart_dontExist() {
        given(shoppingCartService.findByUser(anyLong())).willThrow(ShoppingCartNotInitializedException.class);

        mockMvc.perform(get(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void testGetShoppingCart_userNotFound() {
        given(shoppingCartService.findByUser(anyLong())).willThrow(UserNotFoundException.class);

        mockMvc.perform(get(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(e -> e.getResolvedException().getClass().equals(UserNotFoundException.class));
    }

    @SneakyThrows
    @Test
    void testInitShoppingCart() {
        given(shoppingCartService.initUserShoppingCart(anyLong())).willReturn(shoppingCart);

        mockMvc.perform(post(BASE_URL + "/init")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @SneakyThrows
    @Test
    void testInitShoppingCart_userNotFound() {
        given(shoppingCartService.initUserShoppingCart(anyLong())).willThrow(UserNotFoundException.class);

        mockMvc.perform(post(BASE_URL + "/init")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(e -> e.getResolvedException().getClass().equals(UserNotFoundException.class));
    }

    @SneakyThrows
    @Test
    void testGetShoppingCartItems() {
        List<ShoppingCartItem> items = new ArrayList<>();
        items.add(new ShoppingCartItem());
        items.add(new ShoppingCartItem());
        items.add(new ShoppingCartItem());
        items.add(new ShoppingCartItem());

        given(shoppingCartService.getShoppingCartItems(anyLong(), anyLong())).willReturn(items);

        mockMvc.perform(get(BASE_URL + "/1/cart-items")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", equalTo(4)));

    }

    @SneakyThrows
    @Test
    void testAddItemToShoppingCart() {
        List<ShoppingCartItem> items = new ArrayList<>();
        items.add(new ShoppingCartItem());
        items.add(new ShoppingCartItem());
        items.add(new ShoppingCartItem());
        items.add(new ShoppingCartItem());

        Item item = new Item();
        item.setItemId(5L);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setShoppingCartItems(items);

        given(shoppingCartService.addItemToShoppingCart(anyLong(), anyLong(), anyLong(), anyLong())).willReturn(shoppingCart);

        mockMvc.perform(post(BASE_URL + "/1/cart-items?qty=10&itemId=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(item)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shoppingCartItems.size()", equalTo(4)));
    }

    @SneakyThrows
    @Test
    void testDeleteItemFromShoppingCart() {
        List<ShoppingCartItem> cartItem = new ArrayList<>();
        cartItem.add(new ShoppingCartItem());
        cartItem.add(new ShoppingCartItem());
        cartItem.add(new ShoppingCartItem());
        cartItem.add(new ShoppingCartItem());
        cartItem.add(new ShoppingCartItem());

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setShoppingCartItems(cartItem);


        given(shoppingCartService.deleteItemFromShoppingCart(anyLong(), anyLong(), anyLong())).willReturn(shoppingCart);

        mockMvc.perform(delete(BASE_URL + "/1/cart-items/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shoppingCartItems.size()", equalTo(5)));
    }
}