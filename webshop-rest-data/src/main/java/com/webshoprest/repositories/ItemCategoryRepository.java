package com.webshoprest.repositories;

import com.webshoprest.domain.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {

    Optional<ItemCategory> findByItemCategoryNameIgnoreCase(String categoryName);

    boolean existsByItemCategoryNameIgnoreCase(String categoryName);

}
