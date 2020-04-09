package com.webshoprest.api.v1.services.impl;

import com.webshoprest.api.v1.exceptions.OrderNotFoundException;
import com.webshoprest.api.v1.exceptions.OrderedQuantityException;
import com.webshoprest.api.v1.exceptions.UserDontHaveOrdersException;
import com.webshoprest.api.v1.exceptions.UserNotFoundException;
import com.webshoprest.api.v1.services.ItemService;
import com.webshoprest.api.v1.services.OrderService;
import com.webshoprest.domain.*;
import com.webshoprest.repositories.OrderRepository;
import com.webshoprest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private ItemService itemService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, ItemService itemService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.itemService = itemService;
    }

    @Override
    public List<Order> findAllOrdersFromUser(Long userId) {

        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            if (user.get().getOrders() == null) {
                throw new UserDontHaveOrdersException();
            }
            return user.get().getOrders();
        }

        throw new UserNotFoundException();

    }

    @Override
    public Order findUsersSpecificOrder(Long userId, Long orderId) {

        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            try {
                return user.get().getOrders()
                        .stream()
                        .filter(order -> order.getOrderId().equals(orderId))
                        .findFirst()
                        .get();
            } catch (NullPointerException e) {
                throw new OrderNotFoundException();
            }
        } else {
            throw new UserNotFoundException();
        }

    }

    @Override
    public Order createOrUpdateOrder(Long userId, Map<Long, Long> itemsMap) {

        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            Order order = new Order();

            List<OrderedItem> orderedItems = new ArrayList<>();
            List<OrderItemOrder> orderItemOrderList = new ArrayList<>();

            itemsMap.forEach((itemId, qty) -> {
                Item item = itemService.findById(itemId);
                orderedItems.add(itemToOrderedItem(item, qty));
                if (item.getQty() - qty < 0) {
                    throw new OrderedQuantityException();
                }
                item.setQty(item.getQty() - qty);
                itemService.saveOrUpdateItem(item);
                orderItemOrderList.add(new OrderItemOrder(order, itemToOrderedItem(item, qty)));
            });

            order.setOrderDate(new Date());
            order.setOrderNumber(new OrderNumberGenerator());
            order.setOrderItemOrders(orderItemOrderList);

            Double totalAmount = orderedItems.stream()
                    .mapToDouble(OrderedItem::getTotalItemPrice)
                    .sum();

            order.setTotalAmount(totalAmount);
            order.setUser(user.get());

            return orderRepository.save(order);
        } else {
            throw new UserNotFoundException();
        }

    }

    @Override
    public void deleteOrder(Long userId, Long orderId) {

        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            try {

                user.get().getOrders()
                        .removeIf(order -> order.getOrderId().equals(orderId));

                userRepository.save(user.get());
            } catch (NullPointerException e) {
                throw new OrderNotFoundException();
            }
        } else {
            throw new UserNotFoundException();
        }

    }

    @Override
    public void deleteAllOrders(Long userId) {

        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            try {
                if (user.get().getOrders().isEmpty()) {
                    throw new UserDontHaveOrdersException();
                }

                user.get().setOrders(null);

            } catch (NullPointerException e) {
                throw new UserDontHaveOrdersException();
            }
        } else {
            throw new UserNotFoundException();
        }

    }

    @Override
    public List<OrderedItem> getOrderedItemsFromSpecificOrder(Long userId, Long orderId) {

        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            try {
                return user.get().getOrders()
                        .stream()
                        .filter(order -> order.getOrderId().equals(orderId))
                        .findFirst()
                        .orElseThrow(OrderNotFoundException::new)
                        .getOrderItemOrders()
                        .stream()
                        .map(OrderItemOrder::getOrderItem)
                        .collect(Collectors.toList());
            } catch (NullPointerException e) {
                throw new OrderNotFoundException();
            }
        }

        throw new UserNotFoundException();
    }

    private OrderedItem itemToOrderedItem(Item item, Long orderedQty) {
        OrderedItem orderedItem = new OrderedItem();
        orderedItem.setOrderedQty(orderedQty);
        orderedItem.setOrderedItemName(item.getItemName());
        orderedItem.setOrderedItemPrice(item.getPrice());
        orderedItem.setTotalItemPrice(orderedItem.getOrderedItemPrice() * orderedQty);
        return orderedItem;
    }

}
