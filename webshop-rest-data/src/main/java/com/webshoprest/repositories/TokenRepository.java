package com.webshoprest.repositories;

import com.webshoprest.domain.security.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
