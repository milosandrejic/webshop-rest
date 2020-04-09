package com.webshoprest.api.v1.services;

import com.webshoprest.domain.Order;
import com.webshoprest.domain.OrderedItem;

import java.util.List;
import java.util.Map;

public interface OrderService {

    List<Order> findAllOrdersFromUser(Long userId);

    Order findUsersSpecificOrder(Long userId, Long orderId);

    Order createOrUpdateOrder(Long userId, Map<Long, Long> orderedItems);

    void deleteOrder(Long userId, Long orderId);

    void deleteAllOrders(Long userId);

    List<OrderedItem> getOrderedItemsFromSpecificOrder(Long userId, Long orderId);

}
