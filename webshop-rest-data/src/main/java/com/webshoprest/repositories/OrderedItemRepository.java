package com.webshoprest.repositories;

import com.webshoprest.domain.OrderedItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedItemRepository extends JpaRepository<OrderedItem, Long> {

    OrderedItem findByOrderedItemName(String itemName);

}
