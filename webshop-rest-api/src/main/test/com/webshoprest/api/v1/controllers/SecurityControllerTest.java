package com.webshoprest.api.v1.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.webshoprest.WebshopRestApplication;
import com.webshoprest.api.v1.config.SecurityConfig;
import com.webshoprest.api.v1.exceptions.EntityValidationException;
import com.webshoprest.api.v1.security.GlobalAuthenticationEntryPoint;
import com.webshoprest.api.v1.security.JwtTokenProvider;
import com.webshoprest.api.v1.security.UserAuthenticationEntryPoint;
import com.webshoprest.api.v1.services.impl.UserSecurityService;
import com.webshoprest.api.v1.services.impl.UserServiceImpl;
import com.webshoprest.api.v1.util.SecurityUtility;
import com.webshoprest.api.v1.validators.LoginRequestValidator;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static com.webshoprest.api.v1.controllers.SecurityController.BASE_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {
        WebshopRestApplication.class,
        SecurityConfig.class,
        UserAuthenticationEntryPoint.class,
        GlobalAuthenticationEntryPoint.class,
        SecurityUtility.class,
        UserSecurityService.class,
        JwtTokenProvider.class})
@AutoConfigureMockMvc
@WebMvcTest(SecurityController.class)
class SecurityControllerTest {

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


    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserValidator userValidator;

    @SpyBean
    private LoginRequestValidator loginRequestValidator;

    @Autowired
    private SecurityController securityController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private UserAuthenticationEntryPoint entryPoint;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .alwaysDo(print())
                .build();
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

        mockMvc.perform(post(BASE_URL + "/registration")
                    .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(POST_REQUEST))
                .andExpect(status().isCreated());

    }

    @SneakyThrows
    @Test
    void testCreateUser_validationFail() {
        given(userService.saveOrUpdateUser(any(User.class))).willReturn(new User());
        doThrow(EntityValidationException.class).when(userValidator).validate(any(User.class), any(BindingResult.class));

        mockMvc.perform(post(BASE_URL + "/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new User())))
                .andExpect(status().isBadRequest());

    }

}