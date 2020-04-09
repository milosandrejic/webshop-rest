package com.webshoprest.api.v1.services;

import com.webshoprest.api.v1.exceptions.UserNotFoundException;
import com.webshoprest.api.v1.services.impl.UserServiceImpl;
import com.webshoprest.domain.Address;
import com.webshoprest.domain.City;
import com.webshoprest.domain.Country;
import com.webshoprest.domain.User;
import com.webshoprest.repositories.CityRepository;
import com.webshoprest.repositories.CountryRepository;
import com.webshoprest.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private List<User> users = new ArrayList<>();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        user = new User();
        user.setUserId(1L);
        user.setUsername("username");
    }

    @Test
    void findAll() {
        users.add(user);
        users.add(new User());
        users.add(new User());
        users.add(new User());
        users.add(new User());

        given(userRepository.findAll()).willReturn(users);

        List<User> listOfUsers = userService.findAll();

        assertThat(listOfUsers.size()).isEqualTo(users.size());
        then(userRepository).should(times(1)).findAll();

    }

    @Test
    void findByUsername() {
        given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user));

        User foundUser = userService.findByUsername("username");

        assertThat(foundUser.getUsername()).isEqualTo("username");
        then(userRepository).should(times(1)).findByUsername("username");
    }

    @Test
    void findByUsername_UserNotFound() {
        given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findByUsername("username"));
        then(userRepository).should(times(1)).findByUsername("username");
    }

    @Test
    void findById() {
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        User foundUser = userService.findById(1L);

        assertThat(foundUser.getUserId()).isEqualTo(user.getUserId());
        then(userRepository).should(times(1)).findById(1L);
    }

    @Test
    void findById_UserNotFound(){
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(1L));
        then(userRepository).should(times(1)).findById(1L);
    }

    @Test
    void saveOrUpdateUser() {
        Country country = new Country();
        country.setCountryName("Serbia");

        City city = new City();
        city.setCityName("Belgrade");

        Address address = new Address();

        city.setCountry(country);
        address.setCity(city);
        user.setAddress(address);


        given(userRepository.existsById(anyLong())).willReturn(true);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(userRepository.save(any(User.class))).willReturn(user);
        given(cityRepository.findByCityName(anyString())).willReturn(Optional.of(city));
        given(countryRepository.findByCountryName(anyString())).willReturn(Optional.of(country));

        User savedUser = userService.saveOrUpdateUser(user);

        assertThat(savedUser.getUserId()).isEqualTo(user.getUserId());
        assertThat(savedUser.getAddress().getCity().getCityName()).isEqualTo(city.getCityName());
        assertThat(savedUser.getAddress().getCity().getCountry().getCountryName()).isEqualTo(country.getCountryName());
        then(userRepository).should(times(1)).save(any(User.class));
    }

    @Test
    void deleteById() {
        given(userRepository.existsById(anyLong())).willReturn(true);

        userService.deleteById(1L);

        then(userRepository).should(times(1)).existsById(1L);
        then(userRepository).should(times(1)).deleteById(1L);
    }

    @Test
    void deleteById_UserNotFound(){
        given(userRepository.existsById(anyLong())).willReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteById(1L));
        then(userRepository).should(times(1)).existsById(1L);
    }
}