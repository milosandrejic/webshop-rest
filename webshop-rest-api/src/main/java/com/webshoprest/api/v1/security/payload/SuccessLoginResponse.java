package com.webshoprest.api.v1.security.payload;

import lombok.Data;

@Data
public class SuccessLoginResponse {

    private boolean success;
    private String token;

}
