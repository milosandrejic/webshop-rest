package com.webshoprest.api.v1.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webshoprest.WebshopRestApplication;
import com.webshoprest.api.v1.exceptions.ItemNotFoundException;
import com.webshoprest.api.v1.services.ImageService;
import com.webshoprest.api.v1.services.ItemService;
import com.webshoprest.api.v1.validators.ItemValidator;
import com.webshoprest.domain.Item;
import com.webshoprest.domain.ItemCategory;
import com.webshoprest.domain.ItemLevel;
import com.webshoprest.domain.OrderedItem;
import com.webshoprest.domain.enums.ItemLevels;
import com.webshoprest.domain.enums.UnitOfMeasure;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = WebshopRestApplication.class)
@AutoConfigureMockMvc
@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @MockBean
    private ItemService itemService;
    @MockBean
    private ImageService imageService;
    @SpyBean
    private ItemValidator itemValidator;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemController itemController;

    private Item validItem;

    @BeforeEach
    void setUp() {
        /*mockMvc = MockMvcBuilders.standaloneSetup(itemController)
                .setControllerAdvice(ItemExceptionHandler.class, EntityValidationExceptionHandler.class)
                .build();*/

        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();


        validItem = new Item();
        validItem.setItemId(1L);
        validItem.setItemName("Beer");
        validItem.setDescription("Ipa beer");
        validItem.setPrice(20.00);
        validItem.setUnitOfMeasure(UnitOfMeasure.PIECE);
    }

    @SneakyThrows
    @Test
    void testGetAllItems() {
        given(itemService.findAllItems()).willReturn(List.of(validItem));

        mockMvc.perform(get(ItemController.BASE_PATH)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", equalTo(1)));
    }

    @SneakyThrows
    @Test
    void testGetItemById(){
        given(itemService.findById(anyLong())).willReturn(validItem);

        mockMvc.perform(get(ItemController.BASE_PATH + "/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemId", equalTo(1)));
    }

    @SneakyThrows
    @Test
    void testGetIteById_notFound(){
        given(itemService.findById(anyLong())).willThrow(ItemNotFoundException.class);

        mockMvc.perform(get(ItemController.BASE_PATH + "/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void testSaveNewItem(){
        given(itemService.saveOrUpdateItem(any(Item.class))).willReturn(validItem);

        mockMvc.perform(post(ItemController.BASE_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(validItem)))
                .andExpect(status().isCreated());
    }

    @SneakyThrows
    @Test
    void testSaveNewItem_validationFail(){
        given(itemService.saveOrUpdateItem(any(Item.class))).willReturn(validItem);

        mockMvc.perform(post(ItemController.BASE_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(new Item())))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void testUploadItemImage() {
        given(imageService.uploadImage(any(File.class))).willReturn("image-link");

        MockMultipartFile file = new MockMultipartFile("file-image.png", "some file".getBytes());
        MockPart mockPart = new MockPart("image", file.getBytes());

       mockMvc.perform(multipart(ItemController.BASE_PATH + "/1/upload-image")
                    .part(mockPart)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk());

    }

    @SneakyThrows
    @Test
    void testUpdateItem(){
        given(itemService.saveOrUpdateItem(any(Item.class))).willReturn(validItem);

        mockMvc.perform(put(ItemController.BASE_PATH + "/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(validItem)))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void testUpdateItem_idNull(){
        validItem.setItemId(null);
        given(itemService.saveOrUpdateItem(any(Item.class))).willReturn(validItem);

        mockMvc.perform(put(ItemController.BASE_PATH + "/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(validItem)))
                .andExpect(status().isBadRequest());
    }


    @SneakyThrows
    @Test
    void testUpdateItem_validationFail(){
        given(itemService.saveOrUpdateItem(any(Item.class))).willReturn(validItem);

        Item item = new Item();
        item.setItemId(1L);

        mockMvc.perform(put(ItemController.BASE_PATH + "/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(item)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void testDeleteItem() {
        mockMvc.perform(delete(ItemController.BASE_PATH + "/1"))
                .andExpect(status().isNoContent());
    }

    @SneakyThrows
    @Test
    void testDeleteItem_itemNotFound(){
        doThrow(ItemNotFoundException.class).when(itemService).deleteItemById(anyLong());

        mockMvc.perform(delete(ItemController.BASE_PATH + "/1"))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void testGetItemLevel() {
        ItemLevel itemLevel = new ItemLevel();
        itemLevel.setLevel(ItemLevels.GOLD);

        given(itemService.getLevel(anyLong())).willReturn(itemLevel);

        mockMvc.perform(get(ItemController.BASE_PATH + "/1/item-level"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.level", equalTo("GOLD")));
    }

    @SneakyThrows
    @Test
    void testGetItemLevel_itemNotFound() {
        given(itemService.getLevel(anyLong())).willThrow(ItemNotFoundException.class);

        mockMvc.perform(get(ItemController.BASE_PATH + "/1/item-level"))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void testGetItemCategory() {
        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setItemCategoryName("beer");
        itemCategory.setItemCategoryDescription("desc");

        given(itemService.getCategory(anyLong())).willReturn(itemCategory);

        mockMvc.perform(get(ItemController.BASE_PATH + "/1/item-category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemCategoryName", equalTo("beer")));
    }

    @SneakyThrows
    @Test
    void testGetItemCategory_itemNotFound() {
        given(itemService.getCategory(anyLong())).willThrow(ItemNotFoundException.class);

        mockMvc.perform(get(ItemController.BASE_PATH + "/1/item-category"))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void testGetTopSellingItems() {
        given(itemService.topSellingItems(anyLong())).willReturn(List.of(new OrderedItem(), new OrderedItem()));

        mockMvc.perform(get(ItemController.BASE_PATH + "/top-selling")
                    .param("limit", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", equalTo(2)));
    }
}