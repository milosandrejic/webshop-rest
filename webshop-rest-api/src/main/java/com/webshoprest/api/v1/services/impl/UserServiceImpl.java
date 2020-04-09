package com.webshoprest.api.v1.services.impl;

import com.webshoprest.api.v1.exceptions.UserNotFoundException;
import com.webshoprest.api.v1.services.UserService;
import com.webshoprest.domain.City;
import com.webshoprest.domain.Country;
import com.webshoprest.domain.User;
import com.webshoprest.repositories.CityRepository;
import com.webshoprest.repositories.CountryRepository;
import com.webshoprest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private CityRepository cityRepository;
    private CountryRepository countryRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, CityRepository cityRepository, CountryRepository countryRepository) {
        this.userRepository = userRepository;
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            return user.get();
        }

        throw new UserNotFoundException();
    }

    @Override
    public User findById(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            return user.get();
        }

        throw new UserNotFoundException();
    }

    @Override
    public User saveOrUpdateUser(User user) {
        if (user.getUserId() != null) {
            if (!userRepository.existsById(user.getUserId())) {
                throw new UserNotFoundException();
            }
            //User tempUser = userRepository.getOne(user.getUserId());
            //user.setOrders(tempUser.getOrders());
        }


        Optional<City> city = cityRepository.findByCityName(user.getAddress().getCity().getCityName());

        city.ifPresent(value -> user.getAddress().getCity().setCityId(value.getCityId()));

        Optional<Country> country = countryRepository.findByCountryName(user.getAddress().getCity().getCountry().getCountryName());

        country.ifPresent(value -> user.getAddress().getCity().getCountry().setCountryId(value.getCountryId()));

        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException();
        }
        userRepository.deleteById(id);
    }
}
