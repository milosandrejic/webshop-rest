package com.webshoprest.repositories;

import com.webshoprest.domain.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
