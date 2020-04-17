package com.webshoprest.api.v1.util;

import com.webshoprest.api.v1.services.impl.UserSecurityService;
import com.webshoprest.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class SecurityUtility {

    private final String DEFAULT_PASSWORD = "default password";
    private UserSecurityService userSecurityService;

    private BCryptPasswordEncoder bc = new BCryptPasswordEncoder();

    @Autowired
    public SecurityUtility(UserSecurityService userSecurityService) {
        this.userSecurityService = userSecurityService;
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

}
