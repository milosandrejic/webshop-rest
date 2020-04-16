package com.webshoprest.api.v1.controllers;

import com.webshoprest.api.v1.security.JwtTokenProvider;
import com.webshoprest.api.v1.security.SecurityConstants;
import com.webshoprest.api.v1.security.payload.LoginRequest;
import com.webshoprest.api.v1.security.payload.SuccessLoginResponse;
import com.webshoprest.api.v1.services.UserService;
import com.webshoprest.api.v1.validators.LoginRequestValidator;
import com.webshoprest.api.v1.validators.UserValidator;
import com.webshoprest.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/v1/sec")
@RestController
public class SecurityController {

    private UserService userService;
    private UserValidator userValidator;
    private LoginRequestValidator loginValidator;
    private JwtTokenProvider tokenProvider;
    private AuthenticationManager authenticationManager;

    @Autowired
    public SecurityController(UserService userService, UserValidator userValidator, LoginRequestValidator loginValidator, JwtTokenProvider tokenProvider, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.loginValidator = loginValidator;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/auth")
    public SuccessLoginResponse authenticateUser(@RequestBody @Valid LoginRequest request, BindingResult loginBindingResult){
        loginValidator.validate(request, loginBindingResult);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                  request.getUsername(),
                  request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = SecurityConstants.TOKEN_PREFIX + tokenProvider.generateToken(authentication);

        SuccessLoginResponse loginResponse = new SuccessLoginResponse();
        loginResponse.setSuccess(true);
        loginResponse.setToken(jwtToken);

        return loginResponse;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/registration")
    public User createUser(@Valid @RequestBody User user, BindingResult userBindingResult) {
        userValidator.validate(user, userBindingResult);
        return userService.saveOrUpdateUser(user);
    }
}
