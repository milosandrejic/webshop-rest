package com.webshoprest.repositories;

import com.webshoprest.domain.ShoppingCartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, Long> {

}
