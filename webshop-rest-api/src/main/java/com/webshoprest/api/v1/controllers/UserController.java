package com.webshoprest.api.v1.controllers;

import com.webshoprest.api.v1.services.UserService;
import com.webshoprest.api.v1.validators.UserValidator;
import com.webshoprest.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping(UserController.BASE_URL)
@RestController
public class UserController {

    public final static String BASE_URL = "/api/v1/users";

    private UserService userService;
    private UserValidator userValidator;

    @Autowired
    public UserController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {
        return userService.findById(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/")
    public User getUserByUsername(@RequestParam("username") String username) {
        return userService.findByUsername(username);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    public User createUser(@Valid @RequestBody User user, BindingResult userBindingResult) {
        userValidator.validate(user, userBindingResult);
        return userService.saveOrUpdateUser(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/")
    public User updateUser(@Valid @RequestBody User user, BindingResult userBindingResult){

        userValidator.validateForUpdate(user, userBindingResult);
        return userService.saveOrUpdateUser(user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteById(userId);
    }

}
