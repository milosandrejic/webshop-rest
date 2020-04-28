package com.webshoprest.api.v1.services;

import com.webshoprest.api.v1.exceptions.OrderNotFoundException;
import com.webshoprest.api.v1.exceptions.OrderedQuantityException;
import com.webshoprest.api.v1.exceptions.UserDontHaveOrdersException;
import com.webshoprest.api.v1.exceptions.UserNotFoundException;
import com.webshoprest.api.v1.services.impl.OrderServiceImpl;
import com.webshoprest.domain.*;
import com.webshoprest.repositories.OrderRepository;
import com.webshoprest.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

class OrderServiceTest {


    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemService itemService;

    @InjectMocks
    private OrderServiceImpl orderService;
    private User user;
    private Order order;
    List<Order> orders;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        user = new User();
        user.setUserId(1L);

        order = new Order();
        order.setOrderId(2L);

        orders = new ArrayList<>();
        orders.add(order);

    }

    @Test
    void findAllOrdersFromUser_HaveOrders() {

        orders.add(new Order());
        orders.add(new Order());

        user.setOrders(orders);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        List<Order> listOfOrders = orderService.findAllOrdersFromUser(1L);

        assertThat(listOfOrders.size()).isEqualTo(orders.size());
        then(userRepository).should(times(1)).findById(1L);
    }

    @Test
    void findAllOrdersFromUser_DontHaveOrders() {
        user.setOrders(null);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        assertThrows(UserDontHaveOrdersException.class, () -> orderService.findAllOrdersFromUser(1L));
        then(userRepository).should(times(1)).findById(1L);
    }

    @Test
    void findAllOrdersFromUser_UserNotFound() {
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> orderService.findAllOrdersFromUser(1L));
        then(userRepository).should(times(1)).findById(1L);
    }

    @Test
    void findUsersSpecificOrder_OrderExist() {
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        user.setOrders(orders);

        Order order = orderService.findUsersSpecificOrder(1L, 2L);

        assertThat(order).isNotNull();
        assertThat(order.getOrderId()).isEqualTo(2L);
        then(userRepository).should(times(1)).findById(1L);

    }

    @Test
    void findUsersSpecificOrder_OrderDontExist() {
        user.setOrders(null);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        assertThrows(OrderNotFoundException.class, () -> orderService.findUsersSpecificOrder(1L, 2L));
        then(userRepository).should(times(1)).findById(1L);

    }

    @Test
    void findUserSpecificOrder_UserNotFound() {
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> orderService.findUsersSpecificOrder(1L, 2L));
        then(userRepository).should(times(1)).findById(1L);
    }

    @Test
    void createOrUpdateOrder() {

        Map<Long, Long> orderMap = new HashMap<>();

        Item item1 = new Item();
        item1.setItemId(1L);
        item1.setItemName("name1");
        item1.setPrice(5.00);
        item1.setQty(50L);

        Item item2 = new Item();
        item2.setItemId(2L);
        item2.setItemName("name2");
        item2.setPrice(5.00);
        item2.setQty(50L);

        orderMap.put(item1.getItemId(), 10L);
        orderMap.put(item2.getItemId(), 10L);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(itemService.findById(1L)).willReturn(item1);
        given(itemService.findById(2L)).willReturn(item2);
        given(itemService.saveOrUpdateItem(item1)).willReturn(item1);
        given(itemService.saveOrUpdateItem(item2)).willReturn(item2);
        given(orderRepository.save(any(Order.class))).willAnswer(e -> e.getArgument(0));

        Order order = orderService.createOrUpdateOrder(1L, orderMap);

        assertThat(order.getTotalAmount()).isEqualTo(100.00);
    }

    @Test
    void createOrUpdateOrder_orderedQuantityEexception() {

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

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(itemService.findById(1L)).willReturn(item1);
        given(itemService.findById(2L)).willReturn(item2);

        assertThrows(OrderedQuantityException.class, () -> orderService.createOrUpdateOrder(1L, orderMap));
    }

    @Test
    void createOrUpdateOrder_UserNotFound() {
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        Map<Long, Long> orderMap = new HashMap<>();

        assertThrows(UserNotFoundException.class, () -> orderService.createOrUpdateOrder(1L, orderMap));
        then(userRepository).should(times(1)).findById(1L);

    }

    @Test
    void deleteOrder_OrderPresent_UserPresent() {
        user.setOrders(orders);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        orderService.deleteOrder(1L, 2L);

        then(userRepository).should(times(1)).findById(1L);

    }

    @Test
    void deleteOrder_OrderNotPresent() {
        user.setOrders(orders);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(1L, 22L));
        then(userRepository).should(times(1)).findById(anyLong());

    }

    @Test
    void deleteOrder_UserNotFound() {

        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> orderService.deleteOrder(1L, 2L));
        then(userRepository).should(times(1)).findById(1L);

    }

    @Test
    void deleteAllOrders() {

        List<Order> orders = new ArrayList<>();
        orders.add(new Order());
        orders.add(new Order());
        orders.add(new Order());
        orders.add(new Order());
        orders.add(order);

        user.setOrders(orders);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        orderService.deleteAllOrders(1L);

        assertThat(user.getOrders()).isNull();
        then(userRepository).should(times(1)).findById(1L);

    }

    @Test
    void deleteAllOrders_DontHaveOrders() {

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        assertThrows(UserDontHaveOrdersException.class, () -> orderService.deleteAllOrders(1L));
        then(userRepository).should(times(1)).findById(1L);

    }

    @Test
    void deleteAllOrders_UserNotFound() {
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> orderService.deleteAllOrders(1L));
    }

    @Test
    void getOrderedItemsFromSpecificOrder() {

        List<Order> orders = new ArrayList<>();

        List<OrderItemOrder> orderItemOrders = new ArrayList<>();
        orderItemOrders.add(new OrderItemOrder(new OrderedItem()));
        orderItemOrders.add(new OrderItemOrder(new OrderedItem()));
        orderItemOrders.add(new OrderItemOrder(new OrderedItem()));

        order.setOrderItemOrders(orderItemOrders);
        orders.add(order);

        user.setOrders(orders);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        List<OrderedItem>  orderedItems = orderService.getOrderedItemsFromSpecificOrder(1L, 2L);

        assertThat(orderedItems).isNotNull();
        assertThat(orderedItems.size()).isEqualTo(3L);
        then(userRepository).should(times(1)).findById(1L);

    }

    @Test
    void getOrderedItemsFromSpecificOrder_OrderNotFound() {

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderedItemsFromSpecificOrder(1L, 2L));
        then(userRepository).should(times(1)).findById(1L);

    }

    @Test
    void getOrderedItemsFromSpecificOrder_UserNotFound() {
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> orderService.getOrderedItemsFromSpecificOrder(1L, 2L));
        then(userRepository).should(times(1)).findById(1L);
    }
}