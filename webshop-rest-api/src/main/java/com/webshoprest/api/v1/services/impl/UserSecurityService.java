package com.webshoprest.api.v1.services.impl;

import com.webshoprest.api.v1.exceptions.UserNotFoundException;
import com.webshoprest.domain.User;
import com.webshoprest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserSecurityService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElseThrow(() -> new UsernameNotFoundException("Username not found."));
    }

    public User loadUserById(Long userId){
        Optional<User> user = userRepository.findById(userId);
        return user.orElseThrow(UserNotFoundException::new);
    }

}
