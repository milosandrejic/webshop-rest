package com.webshoprest.api.v1.security;

import com.google.gson.Gson;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class UserAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {

        String jsonResponse = "";
        if (e == null) {
            jsonResponse = new Gson().toJson("Bad credentials.");
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else if (e.getClass().equals(DisabledException.class)) {
            jsonResponse = new Gson().toJson("Email not confirmed.");
            httpServletResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }

        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.getWriter().print(jsonResponse);
    }
}
