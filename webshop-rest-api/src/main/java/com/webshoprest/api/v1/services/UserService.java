package com.webshoprest.api.v1.services;

import com.webshoprest.domain.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findByUsername(String username);

    User findById(Long userId);

    User saveOrUpdateUser(User user);

    void deleteById(Long id);

}
