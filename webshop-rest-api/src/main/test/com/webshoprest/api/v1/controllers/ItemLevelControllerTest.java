package com.webshoprest.api.v1.controllers;

import com.webshoprest.WebshopRestApplication;
import com.webshoprest.api.v1.exceptions.EmptyItemsListException;
import com.webshoprest.api.v1.exceptions.ItemLevelNotFoundException;
import com.webshoprest.api.v1.exceptions.ItemNotFoundException;
import com.webshoprest.api.v1.handlers.ItemExceptionHandler;
import com.webshoprest.api.v1.services.ItemLevelService;
import com.webshoprest.domain.Item;
import com.webshoprest.domain.ItemLevel;
import com.webshoprest.domain.enums.ItemLevels;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = WebshopRestApplication.class)
@AutoConfigureMockMvc
@WebMvcTest(ItemLevelController.class)
class ItemLevelControllerTest {

    @MockBean
    private ItemLevelService itemLevelService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemLevelController itemLevelController;

    private ItemLevel validItemLevel;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(itemLevelController)
            .setControllerAdvice(ItemExceptionHandler.class)
            .build();

        validItemLevel = new ItemLevel();
        validItemLevel.setItemLevelId(1L);
        validItemLevel.setLevel(ItemLevels.GOLD);
    }

    @SneakyThrows
    @Test
    void testGetAllItemLevels() {
        given(itemLevelService.findAll()).willReturn(List.of(new ItemLevel(), new ItemLevel(), new ItemLevel()));

        mockMvc.perform(get(ItemLevelController.BASE_PATH)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", equalTo(3)));
    }

    @SneakyThrows
    @Test
    void testGetItemLevelById_found(){
        given(itemLevelService.findById(anyLong())).willReturn(validItemLevel);

        mockMvc.perform(get(ItemLevelController.BASE_PATH + "/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemLevelId", equalTo(1)));
    }

    @SneakyThrows
    @Test
    void testGetItemLevelById_notFound(){
        given(itemLevelService.findById(anyLong())).willThrow(ItemLevelNotFoundException.class);

        mockMvc.perform(get(ItemLevelController.BASE_PATH + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void testGetItemLevelItems() {
        given(itemLevelService.getItems(anyLong())).willReturn(List.of(new Item(), new Item()));

        mockMvc.perform(get(ItemLevelController.BASE_PATH + "/1/items")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void testGetItemLevelItems_dontHaveItems() {
        given(itemLevelService.getItems(anyLong())).willThrow(EmptyItemsListException.class);

        mockMvc.perform(get(ItemLevelController.BASE_PATH + "/1/items")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void testGetItemLevelItems_itemLevelNotFound() {
        given(itemLevelService.getItems(anyLong())).willThrow(ItemNotFoundException.class);

        mockMvc.perform(get(ItemLevelController.BASE_PATH + "/1/items")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void testGetItemLevelItemsCount() {
        given(itemLevelService.numberOfItems(anyLong())).willReturn(5L);

        mockMvc.perform(get(ItemLevelController.BASE_PATH + "/1/items-count")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo(5)));
    }

    @SneakyThrows
    @Test
    void testGetItemLevelItemsCount_zeroItems() {
        given(itemLevelService.numberOfItems(anyLong())).willReturn(0L);

        mockMvc.perform(get(ItemLevelController.BASE_PATH + "/1/items-count")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo(0)));
    }

    @SneakyThrows
    @Test
    void testGetItemLevelItemsCount_itemLevelNotFound() {
        given(itemLevelService.numberOfItems(anyLong())).willThrow(ItemNotFoundException.class);

        mockMvc.perform(get(ItemLevelController.BASE_PATH + "/1/items-count")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
