package com.webshoprest.repositories;

import com.webshoprest.domain.Order;
import com.webshoprest.domain.OrderItemOrder;
import com.webshoprest.domain.OrderedItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;


    @Test
    void testSaveNewOrderAndOrderItems() {
        Order order1 = new Order();

        OrderedItem orderedItem3 = new OrderedItem();
        orderedItem3.setOrderedItemName("ordered item 3");
        orderedItem3.setOrderedQty(40L);

        OrderedItem orderedItem4 = new OrderedItem();
        orderedItem4.setOrderedItemName("ordered item 4");
        orderedItem4.setOrderedQty(20L);

        OrderItemOrder orderItemOrder3 = new OrderItemOrder();
        orderItemOrder3.setOrder(order1);
        orderItemOrder3.setOrderItem(orderedItem3);

        OrderItemOrder orderItemOrder4 = new OrderItemOrder();
        orderItemOrder4.setOrder(order1);
        orderItemOrder4.setOrderItem(orderedItem4);

        Order order2 = new Order();

        OrderedItem orderedItem1 = new OrderedItem();
        orderedItem1.setOrderedItemName("ordered item 1");
        orderedItem1.setOrderedQty(50L);

        OrderedItem orderedItem2 = new OrderedItem();
        orderedItem2.setOrderedItemName("ordered item 2");
        orderedItem2.setOrderedQty(10L);

        OrderItemOrder orderItemOrder1 = new OrderItemOrder();
        orderItemOrder1.setOrder(order2);
        orderItemOrder1.setOrderItem(orderedItem1);

        OrderItemOrder orderItemOrder2 = new OrderItemOrder();
        orderItemOrder2.setOrder(order2);
        orderItemOrder2.setOrderItem(orderedItem2);

        List<OrderItemOrder> orderItemOrderList = new ArrayList<>();
        orderItemOrderList.add(orderItemOrder1);
        orderItemOrderList.add(orderItemOrder2);

        order2.setOrderItemOrders(orderItemOrderList);

        orderRepository.save(order1);
        orderRepository.save(order2);
    }

    @Test
    void testFindAllOrders() {
        Order order1 = new Order();
        order1.setOrderId(1L);

        Order order2 = new Order();
        order2.setOrderId(2L);

        orderRepository.save(order1);
        orderRepository.save(order2);

        List<Order> orders = orderRepository.findAll();
        assertThat(orders.size()).isEqualTo(2);
    }

    @Test
    void testFindById() {
        Order order1 = new Order();
        order1.setOrderId(1L);

        order1 = orderRepository.save(order1);

        Optional<Order> order = orderRepository.findById(order1.getOrderId());
        assertThat(order.isPresent()).isTrue();
    }

    @Test
    void testDeleteById() {
        Order order1 = new Order();
        order1.setOrderId(1L);
        order1 = orderRepository.save(order1);

        orderRepository.deleteById(order1.getOrderId());
        assertThat(orderRepository.count()).isEqualTo(0);
    }
}