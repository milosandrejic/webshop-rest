package com.webshoprest.api.v1.controllers;

import com.webshoprest.api.v1.services.OrderService;
import com.webshoprest.domain.Order;
import com.webshoprest.domain.OrderedItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1/{userId}/orders")
@RestController
public class OrderController {

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Order> getAllOrdersFromUser(@PathVariable Long userId){
        return orderService.findAllOrdersFromUser(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{orderId}")
    public Order getSpecificOrderFromUser(@PathVariable Long userId,
                                          @PathVariable Long orderId){
        return orderService.findUsersSpecificOrder(userId, orderId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Order createNewOrder(@PathVariable Long userId,
                                @RequestBody Map<Long, Long> orderedItems){

        return orderService.createOrUpdateOrder(userId, orderedItems);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{orderId}")
    public void deleteUserSpecificOrder(@PathVariable Long userId,
                                        @PathVariable Long orderId){
        orderService.deleteOrder(userId, orderId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{orderId}/items")
    public List<OrderedItem> getOrderedItemsFromSpecificOrder(@PathVariable Long userId,
                                                              @PathVariable Long orderId){
        return orderService.getOrderedItemsFromSpecificOrder(userId, orderId);
    }

}
