package com.webshoprest.api.v1.security.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginRequest {

    @NotEmpty
    private String username;
    @NotEmpty
    private String password;

}
