package com.webshoprest.api.v1.util;

import com.webshoprest.api.v1.exceptions.ExpiredTokenException;
import com.webshoprest.api.v1.security.SecurityConstants;
import com.webshoprest.api.v1.services.impl.UserSecurityService;
import com.webshoprest.domain.User;
import com.webshoprest.domain.security.Token;
import com.webshoprest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Component
public class SecurityUtility {

    private final String DEFAULT_PASSWORD = "default password";
    private UserSecurityService userSecurityService;
    private UserRepository userRepository;

    private BCryptPasswordEncoder bc = new BCryptPasswordEncoder();

    @Autowired
    public SecurityUtility(UserSecurityService userSecurityService, UserRepository userRepository) {
        this.userSecurityService = userSecurityService;
        this.userRepository = userRepository;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return bc;
    }

    public void checkIdentity(Principal principal, Long userId){
        User user = (User) userSecurityService.loadUserByUsername(principal.getName());

        if(!user.getUserId().equals(userId)){
            throw new AccessDeniedException("Access denied.");
        }
    }

    public void checkIdentity(Principal principal, String username){
        User user = (User) userSecurityService.loadUserByUsername(principal.getName());

        if(!user.getUsername().equals(username)){
            throw new AccessDeniedException("Access denied.");
        }
    }

    public void checkIdentityWithRole(Principal principal, String username){
        User user = (User) userSecurityService.loadUserByUsername(principal.getName());

        if(user.getRole().getRole().name().startsWith("CUSTOMER")){
            if(!user.getUsername().equals(username)){
                throw new AccessDeniedException("Access denied.");
            }
        }
    }

    public void checkIdentityWithRole(Principal principal, Long userId){
        User user = (User) userSecurityService.loadUserByUsername(principal.getName());

        if(user.getRole().getRole().name().startsWith("CUSTOMER")){
            if(!user.getUserId().equals(userId)){
                throw new AccessDeniedException("Access denied.");
            }
        }
    }

    public Token generateToken(){
        Token token = new Token();
        token.setToken(UUID.randomUUID().toString());
        token.setExpiration(SecurityConstants.EMAIL_TOKEN_EXPIRATION);
        return token;
    }

    public void validateToken(Token token) {
        boolean expired = (new Date().getTime() - token.getCreated()) > token.getExpiration();
        Optional<User> user = userRepository.findByToken(token.getToken());

        if(expired) {
            user.ifPresent(e -> e.setToken(null));
            userRepository.save(user.get());
            throw new ExpiredTokenException();
        }
    }

}
