package com.webshoprest.api.v1.controllers;

import com.webshoprest.WebshopRestApplication;
import com.webshoprest.api.v1.config.SecurityConfig;
import com.webshoprest.api.v1.exceptions.EntityValidationException;
import com.webshoprest.api.v1.exceptions.UserNotFoundException;
import com.webshoprest.api.v1.security.GlobalAuthenticationEntryPoint;
import com.webshoprest.api.v1.security.JwtTokenProvider;
import com.webshoprest.api.v1.security.UserAuthenticationEntryPoint;
import com.webshoprest.api.v1.services.impl.UserSecurityService;
import com.webshoprest.api.v1.services.impl.UserServiceImpl;
import com.webshoprest.api.v1.util.SecurityUtility;
import com.webshoprest.api.v1.validators.UserValidator;
import com.webshoprest.domain.Address;
import com.webshoprest.domain.City;
import com.webshoprest.domain.Country;
import com.webshoprest.domain.User;
import com.webshoprest.domain.enums.Roles;
import com.webshoprest.repositories.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.webshoprest.api.v1.TestConfig.initAuthentication;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {
        WebshopRestApplication.class,
        SecurityConfig.class,
        UserAuthenticationEntryPoint.class,
        GlobalAuthenticationEntryPoint.class,
        BCryptPasswordEncoder.class,
        JwtTokenProvider.class})
@AutoConfigureMockMvc
@WebMvcTest(value = UserController.class)
class UserControllerTest {

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
    @MockBean
    private UserValidator userValidator;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SecurityUtility securityUtility;
    @MockBean
    private UserSecurityService userSecurityService;

    @Autowired
    private WebApplicationContext context;

    private User userOne;
    private List<User> listOfUsers = new ArrayList<>();


    @BeforeEach
    void setUp() {
        userOne = new User();
        userOne.setUserId((long) 1);
        userOne.setUsername("username1");

        User userTwo = new User();
        userTwo.setUserId((long) 2);
        userTwo.setUsername("username2");

        listOfUsers.add(userOne);
        listOfUsers.add(userTwo);

        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .alwaysDo(print())
                .build();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @SneakyThrows
    @Test
    void testGetAllUsers() {
        initAuthentication(Roles.ADMIN);
        given(userController.getAllUsers()).willReturn(listOfUsers);
        given(userService.findAll()).willReturn(listOfUsers);

        mockMvc.perform(get(UserController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.size()", equalTo(listOfUsers.size())))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @SneakyThrows
    @Test
    void testGetUserById_found_withAdminRole() {
        initAuthentication(Roles.ADMIN);
        given(userService.findById(anyLong())).willReturn(userOne);

        mockMvc.perform(get(UserController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.userId", equalTo(1)));
    }

    @SneakyThrows
    @Test
    void testGetUserById_found_withCustomerRole() {
        initAuthentication(Roles.CUSTOMER_GOLD);
        given(userService.findById(anyLong())).willReturn(userOne);

        mockMvc.perform(get(UserController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.userId", equalTo(1)));
    }

    @SneakyThrows
    @Test
    void testGetUserById_found_validationFail() {
        Principal principal = () -> "username";
        initAuthentication(Roles.CUSTOMER_GOLD);

        doThrow(AccessDeniedException.class).when(securityUtility).checkIdentityWithRole(principal, 1L);

        mockMvc.perform(get(UserController.BASE_URL + "/1")
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }


    @SneakyThrows
    @Test
    void testGetUserById_notFound() {
        initAuthentication(Roles.ADMIN);
        given(userService.findById(anyLong())).willThrow(UserNotFoundException.class);

        mockMvc.perform(get(UserController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void testGetUserByUsername() {
        given(userService.findByUsername(anyString())).willReturn(userOne);

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
    void testUpdateUser_validationPass() {
        initAuthentication(Roles.CUSTOMER_GOLD);

        Principal principal = () -> "marry123";

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

        mockMvc.perform(put(UserController.BASE_URL + "/")
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(PUT_REQUEST))
                .andExpect(status().isOk());

    }

    @SneakyThrows
    @Test
    void testUpdateUser_validationFail() {
        initAuthentication(Roles.CUSTOMER_GOLD);
        Principal principal = () -> "marry123";

        given(userService.saveOrUpdateUser(userOne)).willReturn(userOne);
        doThrow(EntityValidationException.class).when(userValidator).validateForUpdate(any(User.class), any(BindingResult.class));

        mockMvc.perform(put(UserController.BASE_URL + "/")
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(PUT_REQUEST))
                .andExpect(status().isBadRequest());

    }

    @SneakyThrows
    @Test
    void testDeleteUser() {
        initAuthentication(Roles.ADMIN);
        given(userRepository.existsById(anyLong())).willReturn(true);

        mockMvc.perform(delete(UserController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));

        then(userService).should(times(1)).deleteById(1L);
    }

    @SneakyThrows
    @Test
    void testDeleteUser_userNotFound() {
        initAuthentication(Roles.ADMIN);
        doThrow(UserNotFoundException.class).when(userService).deleteById(anyLong());

        mockMvc.perform(delete(UserController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}