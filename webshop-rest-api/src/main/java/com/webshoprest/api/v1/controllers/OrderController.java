package com.webshoprest.api.v1.controllers;

import com.webshoprest.api.v1.services.OrderService;
import com.webshoprest.api.v1.util.SecurityUtility;
import com.webshoprest.domain.Order;
import com.webshoprest.domain.OrderedItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import static com.webshoprest.api.v1.security.SecurityConstants.CUSTOMER_AUTHORITY_STRINGS;

@RequestMapping("/api/v1/{userId}/orders")
@RestController
public class OrderController {

    private OrderService orderService;
    private SecurityUtility securityUtility;

    @Autowired
    public OrderController(OrderService orderService, SecurityUtility securityUtility) {
        this.orderService = orderService;
        this.securityUtility = securityUtility;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Order> getAllOrdersFromUser(@PathVariable Long userId, Principal principal){
        securityUtility.checkIdentityWithRole(principal, userId);
        return orderService.findAllOrdersFromUser(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{orderId}")
    public Order getSpecificOrderFromUser(@PathVariable Long userId,
                                          @PathVariable Long orderId,
                                          Principal principal){

        securityUtility.checkIdentityWithRole(principal, userId);
        return orderService.findUsersSpecificOrder(userId, orderId);
    }

    @PreAuthorize(CUSTOMER_AUTHORITY_STRINGS)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Order createNewOrder(@PathVariable Long userId,
                                @RequestBody Map<Long, Long> orderedItems,
                                Principal principal){

        securityUtility.checkIdentity(principal, userId);
        return orderService.createOrUpdateOrder(userId, orderedItems);
    }

    @PreAuthorize(CUSTOMER_AUTHORITY_STRINGS)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{orderId}")
    public void deleteUserSpecificOrder(@PathVariable Long userId,
                                        @PathVariable Long orderId,
                                        Principal principal){
        securityUtility.checkIdentity(principal, userId);
        orderService.deleteOrder(userId, orderId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{orderId}/items")
    public List<OrderedItem> getOrderedItemsFromSpecificOrder(@PathVariable Long userId,
                                                              @PathVariable Long orderId,
                                                              Principal principal){
        securityUtility.checkIdentityWithRole(principal, userId);
        return orderService.getOrderedItemsFromSpecificOrder(userId, orderId);
    }

}
