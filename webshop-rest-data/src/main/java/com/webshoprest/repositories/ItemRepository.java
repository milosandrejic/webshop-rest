package com.webshoprest.repositories;

import com.webshoprest.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

    boolean existsById(Long itemId);

}
