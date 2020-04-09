package com.webshoprest.api.v1.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.webshoprest.WebshopRestApplication;
import com.webshoprest.api.v1.exceptions.UserNotFoundException;
import com.webshoprest.api.v1.handlers.EntityValidationExceptionHandler;
import com.webshoprest.api.v1.handlers.UserExceptionHandler;
import com.webshoprest.api.v1.services.impl.UserServiceImpl;
import com.webshoprest.api.v1.validators.UserValidator;
import com.webshoprest.domain.Address;
import com.webshoprest.domain.City;
import com.webshoprest.domain.Country;
import com.webshoprest.domain.User;
import com.webshoprest.repositories.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = WebshopRestApplication.class)
@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
class UserControllerTest {

    private final String POST_REQUEST = "        {\n" +
            "            \"firstName\": \"Marry\",\n" +
            "            \"lastName\": \"Public\",\n" +
            "            \"dob\": \"1990-06-10\",\n" +
            "            \"email\": \"marrypublic@gmail.com\",\n" +
            "            \"username\": \"marry123\",\n" +
            "            \"password\": \"password\",\n" +
            "            \"phoneNumber\": \"1234521312\",\n" +
            "            \"totalSpent\": null,\n" +
            "            \"address\": {\n" +
            "                \"street\": \"Street\",\n" +
            "                \"streetNumber\": 15,\n" +
            "                \"city\": {\n" +
            "                    \"cityName\": \"New York\",\n" +
            "                    \"country\": {\n" +
            "                        \"countryName\": \"USA\"\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        }";
    private final String PUT_REQUEST = "        {\n" +
            "               \"id\": \"1\",\n" +
            "            \"firstName\": \"Marry\",\n" +
            "            \"lastName\": \"Public\",\n" +
            "            \"dob\": \"1990-06-10\",\n" +
            "            \"email\": \"marrypublic@gmail.com\",\n" +
            "            \"username\": \"marry123\",\n" +
            "            \"password\": \"password\",\n" +
            "            \"phoneNumber\": \"1234521312\",\n" +
            "            \"totalSpent\": null,\n" +
            "            \"address\": {\n" +
            "                \"street\": \"Street\",\n" +
            "                \"streetNumber\": 15,\n" +
            "                \"city\": {\n" +
            "                    \"cityName\": \"New York\",\n" +
            "                    \"country\": {\n" +
            "                        \"countryName\": \"USA\"\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        }";
    @MockBean
    private UserServiceImpl userService;
    @Autowired
    private UserController userController;
    @MockBean
    private UserRepository userRepository;
    @SpyBean
    private UserValidator userValidator;
    @Autowired
    private MockMvc mockMvc;
    private User user;
    private List<User> listOfUsers = new ArrayList<>();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(UserExceptionHandler.class, EntityValidationExceptionHandler.class)
                .build();

        user = new User();
        user.setUserId((long) 1);
        user.setUsername("username1");

        User userTwo = new User();
        userTwo.setUserId((long) 2);
        userTwo.setUsername("username2");

        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(userTwo);

        listOfUsers.add(user);
        listOfUsers.add(userTwo);
    }

    @SneakyThrows
    @Test
    void testGetAllUsers() {
        given(userService.findAll()).willReturn(listOfUsers);

        mockMvc.perform(get(UserController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.size()", equalTo(listOfUsers.size())));
    }

    @SneakyThrows
    @Test
    void testGetUserById_found() {
        given(userService.findById(anyLong())).willReturn(user);

        mockMvc.perform(get(UserController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.userId", equalTo(1)));
    }

    @SneakyThrows
    @Test
    void testGetUserById_notFound() {
        given(userService.findById(anyLong())).willThrow(UserNotFoundException.class);

        mockMvc.perform(get(UserController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void testGetUserByUsername() {
        given(userService.findByUsername(anyString())).willReturn(user);

        mockMvc.perform(get(UserController.BASE_URL + "/?username=username1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.username", equalTo("username1")));
    }

    @SneakyThrows
    @Test
    void testGetUserByUsername_notFound() {
        given(userService.findByUsername(anyString())).willThrow(UserNotFoundException.class);

        mockMvc.perform(get(UserController.BASE_URL + "/?username=username1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void testCreateUser_validationPass() {

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername("username");
        user.setPassword("password");
        user.setDob(LocalDate.of(2000, 2, 2));
        user.setEmail("johndoe@gmail.com");
        user.setPhoneNumber("13245681");

        Country country = new Country();
        country.setCountryName("Serbia");

        City city = new City();
        city.setCityName("Belgrade");
        city.setCountry(country);

        Address address = new Address();
        address.setStreet("Street");
        address.setStreetNumber(15L);
        address.setCity(city);

        user.setAddress(address);

        given(userService.saveOrUpdateUser(user)).willReturn(user);

        System.out.println(new Gson().toJson(user));

        mockMvc.perform(post(UserController.BASE_URL + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(POST_REQUEST))
                .andExpect(status().isCreated());

    }

    @SneakyThrows
    @Test
    void testCreateUser_validationFail() {
        given(userService.saveOrUpdateUser(user)).willReturn(user);

        mockMvc.perform(post(UserController.BASE_URL + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new User())))
                .andExpect(status().isBadRequest());

    }

    @SneakyThrows
    @Test
    void testUpdateUser_validationPass() {
        User user = new User();
        user.setUserId(5L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername("username");
        user.setPassword("password");
        user.setDob(LocalDate.of(2000, 2, 2));
        user.setEmail("johndoe@gmail.com");
        user.setPhoneNumber("12345850");

        Country country = new Country();
        country.setCountryName("Serbia");

        City city = new City();
        city.setCityName("Belgrade");

        Address address = new Address();
        address.setStreet("Street");
        address.setStreetNumber(15L);

        user.setAddress(address);

        given(userService.saveOrUpdateUser(user)).willReturn(user);

        MvcResult result = mockMvc.perform(put(UserController.BASE_URL + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(PUT_REQUEST))
                .andExpect(status().isOk())
                .andReturn();

    }

    @SneakyThrows
    @Test
    void testUpdateUser_validationFail() {
        given(userService.saveOrUpdateUser(user)).willReturn(user);

        mockMvc.perform(put(UserController.BASE_URL + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new User())))
                .andExpect(status().isBadRequest());

    }

    @SneakyThrows
    @Test
    void testDeleteUser() {

        given(userRepository.existsById(anyLong())).willReturn(true);

        mockMvc.perform(delete(UserController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));

        then(userService).should(times(1)).deleteById(1L);
    }

    @SneakyThrows
    @Test
    void testDeleteUser_userNotFound() {

        doThrow(UserNotFoundException.class).when(userService).deleteById(anyLong());

        mockMvc.perform(delete(UserController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}