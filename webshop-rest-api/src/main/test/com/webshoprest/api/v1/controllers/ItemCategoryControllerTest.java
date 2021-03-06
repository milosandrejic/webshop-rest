package com.webshoprest.api.v1.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webshoprest.WebshopRestApplication;
import com.webshoprest.api.v1.config.SecurityConfig;
import com.webshoprest.api.v1.exceptions.EmptyItemsListException;
import com.webshoprest.api.v1.exceptions.ItemCategoryNotFoundException;
import com.webshoprest.api.v1.security.GlobalAuthenticationEntryPoint;
import com.webshoprest.api.v1.security.JwtTokenProvider;
import com.webshoprest.api.v1.security.UserAuthenticationEntryPoint;
import com.webshoprest.api.v1.services.ItemCategoryService;
import com.webshoprest.api.v1.services.impl.UserSecurityService;
import com.webshoprest.api.v1.validators.ItemCategoryValidator;
import com.webshoprest.domain.Item;
import com.webshoprest.domain.ItemCategory;
import com.webshoprest.domain.enums.Roles;
import com.webshoprest.repositories.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static com.webshoprest.api.v1.TestConfig.initAuthentication;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
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
@WebMvcTest(ItemCategoryController.class)
class ItemCategoryControllerTest {

    @MockBean
    private ItemCategoryService itemCategoryService;

    @SpyBean
    private ItemCategoryValidator itemCategoryValidator;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserRepository userRepository;

    ItemCategory validItemCategory = new ItemCategory();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .alwaysDo(print())
                .build();

        validItemCategory.setItemCategoryId(1L);
        validItemCategory.setItemCategoryName("Food");
        validItemCategory.setItemCategoryDescription("Food description");

    }

    @SneakyThrows
    @Test
    void testGetAllItemCategories() {
        given(itemCategoryService.findAll()).willReturn(List.of(validItemCategory));

        mockMvc.perform(get(ItemCategoryController.BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", equalTo(1)));
    }

    @SneakyThrows
    @Test
    void testGetItemCategoryByName_found() {
        given(itemCategoryService.findById(anyLong())).willReturn(validItemCategory);

        mockMvc.perform(get(ItemCategoryController.BASE_URL + "/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemCategoryName", equalTo(validItemCategory.getItemCategoryName())));
    }

    @SneakyThrows
    @Test
    void testGetItemCategoryByName_notFound() {
        given(itemCategoryService.findById(anyLong())).willThrow(ItemCategoryNotFoundException.class);

        mockMvc.perform(get(ItemCategoryController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void testCreateNewItemCategory_validationPass() {
        initAuthentication(Roles.ADMIN);

        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setItemCategoryName("category");
        itemCategory.setItemCategoryDescription("desc");

        given(itemCategoryService.saveOrUpdateCategory(any(ItemCategory.class))).willReturn(validItemCategory);

        mockMvc.perform(post(ItemCategoryController.BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(itemCategory)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.itemCategoryName", equalTo(validItemCategory.getItemCategoryName())));
    }

    @SneakyThrows
    @Test
    void testCreateItemCategory_validationFail() {
        initAuthentication(Roles.ADMIN);

        given(itemCategoryService.saveOrUpdateCategory(any(ItemCategory.class))).willReturn(validItemCategory);

        mockMvc.perform(post(ItemCategoryController.BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(new ItemCategory())))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void testUpdateItemCategory_validationFail() {
        initAuthentication(Roles.ADMIN);

        given(itemCategoryService.saveOrUpdateCategory(any(ItemCategory.class))).willReturn(validItemCategory);

        mockMvc.perform(put(ItemCategoryController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new ItemCategory())))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void testUpdateNewItemCategory_validationPass() {
        initAuthentication(Roles.ADMIN);

        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setItemCategoryName("category");
        itemCategory.setItemCategoryDescription("desc");

        given(itemCategoryService.saveOrUpdateCategory(any(ItemCategory.class))).willReturn(validItemCategory);

        mockMvc.perform(post(ItemCategoryController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(itemCategory)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.itemCategoryName", equalTo(validItemCategory.getItemCategoryName())));
    }

    @SneakyThrows
    @Test
    void testUpdateItemCategory_itemCategoryNotFound() {
        initAuthentication(Roles.ADMIN);

        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setItemCategoryName("category");
        itemCategory.setItemCategoryDescription("desc");

        given(itemCategoryService.saveOrUpdateCategory(any(ItemCategory.class))).willThrow(ItemCategoryNotFoundException.class);

        mockMvc.perform(put(ItemCategoryController.BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(new ItemCategory())))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void testDeleteItemCategory(){
        initAuthentication(Roles.ADMIN);

        mockMvc.perform(delete(ItemCategoryController.BASE_URL + "/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @SneakyThrows
    @Test
    void testDeleteItemCategory_itemCategoryNotFound(){
        initAuthentication(Roles.ADMIN);

        doThrow(ItemCategoryNotFoundException.class).when(itemCategoryService).deleteById(anyLong());

        mockMvc.perform(delete(ItemCategoryController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void testGetItemCategoryItems() {
        given(itemCategoryService.getItems(anyLong())).willReturn(List.of(new Item(), new Item(), new Item()));

        mockMvc.perform(get(ItemCategoryController.BASE_URL + "/1/items")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", equalTo(3)));
    }

    @SneakyThrows
    @Test
    void testGetItemCategoryItems_itemCategoryNotFound() {
        given(itemCategoryService.getItems(anyLong())).willThrow(ItemCategoryNotFoundException.class);

        mockMvc.perform(get(ItemCategoryController.BASE_URL + "/1/items")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void testGetItemCategoryItems_dontHaveItems() {
        given(itemCategoryService.getItems(anyLong())).willThrow(EmptyItemsListException.class);

        mockMvc.perform(get(ItemCategoryController.BASE_URL + "/1/items")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void testGetItemCategoryItemsCount() {
        given(itemCategoryService.numberOfItems(anyLong())).willReturn(5L);

        mockMvc.perform(get(ItemCategoryController.BASE_URL + "/1/items-count")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo(5)));
    }

    @SneakyThrows
    @Test
    void testGetItemCategoryItemsCount_itemCategoryNotFound() {
        given(itemCategoryService.numberOfItems(anyLong())).willThrow(ItemCategoryNotFoundException.class);

        mockMvc.perform(get(ItemCategoryController.BASE_URL + "/1/items-count")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void testGetItemCategoryItemsCount_dontHaveItems() {
        given(itemCategoryService.numberOfItems(anyLong())).willReturn(0L);

        mockMvc.perform(get(ItemCategoryController.BASE_URL + "/1/items-count")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo(0)));
    }
}