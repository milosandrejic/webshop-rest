package com.webshoprest.api.v1.util;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtility {

    private final String DEFAULT_PASSWORD = "default password";

    private BCryptPasswordEncoder bc = new BCryptPasswordEncoder();

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return bc;
    }


}
