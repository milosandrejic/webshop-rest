package com.webshoprest.api.v1.controllers;

import com.webshoprest.api.v1.services.UserService;
import com.webshoprest.api.v1.util.SecurityUtility;
import com.webshoprest.api.v1.validators.UserValidator;
import com.webshoprest.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static com.webshoprest.api.v1.security.SecurityConstants.ADMIN_AUTHORITY_STRING;
import static com.webshoprest.api.v1.security.SecurityConstants.CUSTOMER_AUTHORITY_STRINGS;

@RequestMapping(UserController.BASE_URL)
@RestController
public class UserController {

    public final static String BASE_URL = "/api/v1/users";

    private UserService userService;
    private UserValidator userValidator;
    private SecurityUtility securityUtility;

    @Autowired
    public UserController(UserService userService, UserValidator userValidator, SecurityUtility securityUtility) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.securityUtility = securityUtility;
    }

    @PreAuthorize(ADMIN_AUTHORITY_STRING)
    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId, Principal principal) {
        securityUtility.checkIdentityWithRole(principal, userId);
        return userService.findById(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/")
    public User getUserByUsername(@RequestParam("username") String username, Principal principal) {
        securityUtility.checkIdentityWithRole(principal, username);
        return userService.findByUsername(username);
    }

    @PreAuthorize(CUSTOMER_AUTHORITY_STRINGS)
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/")
    public User updateUser(@Valid @RequestBody User user, BindingResult userBindingResult, Principal principal){
        userValidator.validateForUpdate(user, userBindingResult);

        if(!user.getUsername().equals(principal.getName())){
            throw new AccessDeniedException("Access denied.");
        }

        return userService.saveOrUpdateUser(user);
    }

    @PreAuthorize(ADMIN_AUTHORITY_STRING)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteById(userId);
    }

}
