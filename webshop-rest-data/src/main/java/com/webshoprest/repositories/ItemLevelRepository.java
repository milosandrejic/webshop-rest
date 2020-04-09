package com.webshoprest.repositories;

import com.webshoprest.domain.ItemLevel;
import com.webshoprest.domain.enums.ItemLevels;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemLevelRepository extends JpaRepository<ItemLevel, Long> {
   Optional<ItemLevel> findByLevel(ItemLevels level);
}
