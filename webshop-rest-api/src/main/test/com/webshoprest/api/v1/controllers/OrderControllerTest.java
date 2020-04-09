package com.webshoprest.api.v1.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webshoprest.WebshopRestApplication;
import com.webshoprest.api.v1.exceptions.OrderNotFoundException;
import com.webshoprest.api.v1.exceptions.UserNotFoundException;
import com.webshoprest.api.v1.services.OrderService;
import com.webshoprest.domain.Item;
import com.webshoprest.domain.Order;
import com.webshoprest.domain.OrderNumberGenerator;
import com.webshoprest.domain.OrderedItem;
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
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = WebshopRestApplication.class)
@AutoConfigureMockMvc
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    public final String BASE_URL = "/api/v1/1/orders";

    @MockBean
    private OrderService orderService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private OrderController orderController;

    private Order validOrder;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .alwaysDo(print())
                .build();

        validOrder = new Order();
        validOrder.setOrderId(1L);
        validOrder.setOrderNumber(new OrderNumberGenerator());
        validOrder.setOrderDate(new Date());
        validOrder.setTotalAmount(20.00);
    }

    @SneakyThrows
    @Test
    void testGetAllOrdersFromUser() {
        given(orderService.findAllOrdersFromUser(anyLong())).willReturn(List.of(new Order(), new Order()));

        mockMvc.perform(get(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.size()", equalTo(2)));
    }

    @SneakyThrows
    @Test
    void testGetAllOrdersFromUser_userNotFound() {
        given(orderService.findAllOrdersFromUser(anyLong())).willThrow(UserNotFoundException.class);

        mockMvc.perform(get(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void testGetSpecificOrderFromUser() {
        given(orderService.findUsersSpecificOrder(anyLong(), anyLong())).willReturn(validOrder);

        mockMvc.perform(get(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.orderId", equalTo(1)));
    }

    @SneakyThrows
    @Test
    void testGetSpecificOrderFromUser_userNotFound() {
        given(orderService.findUsersSpecificOrder(anyLong(), anyLong())).willThrow(UserNotFoundException.class);

        mockMvc.perform(get(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void testGetSpecificOrderFromUser_orderNotFound() {
        given(orderService.findUsersSpecificOrder(anyLong(), anyLong())).willThrow(OrderNotFoundException.class);

        mockMvc.perform(get(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void testCreateOrder() {
        Map<Long, Long> orderMap = new HashMap<>();

        Item item1 = new Item();
        item1.setItemId(1L);
        item1.setItemName("name");
        item1.setPrice(5.00);
        item1.setQty(50L);

        Item item2 = new Item();
        item2.setItemId(2L);
        item2.setItemName("name");
        item2.setPrice(5.00);
        item2.setQty(50L);

        orderMap.put(item1.getItemId(), 10L);
        orderMap.put(item2.getItemId(), 10L);

        given(orderService.createOrUpdateOrder(1L, orderMap)).willReturn(validOrder);

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(orderMap)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId", equalTo(1)));
    }

    @SneakyThrows
    @Test
    void testCreateOrder_userNotFound() {
        Map<Long, Long> orderMap = new HashMap<>();

        Item item1 = new Item();
        item1.setItemId(1L);
        item1.setItemName("name");
        item1.setPrice(5.00);
        item1.setQty(50L);

        Item item2 = new Item();
        item2.setItemId(2L);
        item2.setItemName("name");
        item2.setPrice(5.00);
        item2.setQty(50L);

        orderMap.put(item1.getItemId(), 150L);
        orderMap.put(item2.getItemId(), 10L);

        given(orderService.createOrUpdateOrder(1L, orderMap)).willThrow(UserNotFoundException.class);

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(orderMap)))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void testCreateOrder_orderAlreadyCreated() {
        given(orderService.findUsersSpecificOrder(anyLong(), anyLong())).willThrow(OrderNotFoundException.class);

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(validOrder)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void testDeleteOrder() {
        mockMvc.perform(delete(BASE_URL + "/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        then(orderService).should(times(1)).deleteOrder(1L, 1L);
    }

    @SneakyThrows
    @Test
    void testDeleteOrder_userNotFound() {
        doThrow(UserNotFoundException.class).when(orderService).deleteOrder(anyLong(), anyLong());

        mockMvc.perform(delete(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        then(orderService).should(times(1)).deleteOrder(1L, 1L);
    }

    @SneakyThrows
    @Test
    void testDeleteOrder_orderNotFound() {
        doThrow(OrderNotFoundException.class).when(orderService).deleteOrder(anyLong(), anyLong());

        mockMvc.perform(delete(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        then(orderService).should(times(1)).deleteOrder(1L, 1L);
    }

    @SneakyThrows
    @Test
    void testGetItemsFromSpecificOrder() {
        given(orderService.getOrderedItemsFromSpecificOrder(anyLong(), anyLong())).willReturn(List.of(new OrderedItem(), new OrderedItem()));

        mockMvc.perform(get(BASE_URL + "/1/items")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", equalTo(2)));
    }

    @SneakyThrows
    @Test
    void testGetItemsFromSpecificOrder_userNotFound() {
        given(orderService.getOrderedItemsFromSpecificOrder(anyLong(), anyLong())).willThrow(UserNotFoundException.class);

        mockMvc.perform(get(BASE_URL + "/1/items")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void testGetItemsFromSpecificOrder_orderNotFound() {
        given(orderService.getOrderedItemsFromSpecificOrder(anyLong(), anyLong())).willThrow(OrderNotFoundException.class);

        mockMvc.perform(get(BASE_URL + "/1/items")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}