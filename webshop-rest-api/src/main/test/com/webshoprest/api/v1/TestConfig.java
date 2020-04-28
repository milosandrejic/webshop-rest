package com.webshoprest.api.v1;

import com.webshoprest.domain.User;
import com.webshoprest.domain.enums.Roles;
import com.webshoprest.domain.security.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class TestConfig {

    public static void initAuthentication(Roles roles) {
        User user = new User();
        user.setUserId(10L);
        user.setUsername("username");

        Role role = new Role();
        role.setRole(roles);
        user.setRole(role);


        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


}
