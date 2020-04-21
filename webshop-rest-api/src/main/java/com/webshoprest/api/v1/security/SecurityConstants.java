package com.webshoprest.api.v1.security;

public class SecurityConstants {

    public static final String [] PUBLIC_PATHS = {
            "/api/v1/sec/**"
    };

    public static final String USER_AUTH_PATH = "/api/v1/sec/auth";
    public static final String H2_URL = "/h2-console/**";
    public static final String SECRET = "SecretJwtKey";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final long TOKEN_EXPIRATION = 1000 * 60 * 60 * 24;
    public static final String ADMIN_AUTHORITY_STRING = "hasAuthority('ADMIN')";
    public static final String CUSTOMER_AUTHORITY_STRINGS = "hasAnyAuthority('CUSTOMER_STANDARD', 'CUSTOMER_GOLD', 'CUSTOMER_PREMIUM')";
    public static final long EMAIL_TOKEN_EXPIRATION = 1000 * 60; //15 minutes

}
