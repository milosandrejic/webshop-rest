package com.webshoprest.api.v1.services.impl;

import com.webshoprest.api.v1.exceptions.UserNotFoundException;
import com.webshoprest.api.v1.services.EmailService;
import com.webshoprest.api.v1.services.UserService;
import com.webshoprest.api.v1.util.EmailContentUtil;
import com.webshoprest.api.v1.util.EmailType;
import com.webshoprest.api.v1.util.SecurityUtility;
import com.webshoprest.domain.City;
import com.webshoprest.domain.Country;
import com.webshoprest.domain.User;
import com.webshoprest.domain.security.Token;
import com.webshoprest.repositories.CityRepository;
import com.webshoprest.repositories.CountryRepository;
import com.webshoprest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private CityRepository cityRepository;
    private CountryRepository countryRepository;
    private EmailService emailService;
    private HttpServletRequest request;
    private BCryptPasswordEncoder passwordEncoder;
    private SecurityUtility securityUtility;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, CityRepository cityRepository, CountryRepository countryRepository, EmailService emailService, HttpServletRequest request, BCryptPasswordEncoder passwordEncoder, SecurityUtility securityUtility) {
        this.userRepository = userRepository;
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.emailService = emailService;
        this.request = request;
        this.passwordEncoder = passwordEncoder;
        this.securityUtility = securityUtility;
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

    @Transactional
    @Override
    public User saveOrUpdateUser(User user) {

        Optional<City> city = cityRepository.findByCityName(user.getAddress().getCity().getCityName());

        city.ifPresentOrElse(
                presentCity -> user.getAddress().getCity().setCityId(presentCity.getCityId()),
                () -> user.getAddress().getCity().setCityId(null));

        Optional<Country> country = countryRepository.findByCountryName(user.getAddress().getCity().getCountry().getCountryName());

        country.ifPresentOrElse(
                presentCountry -> user.getAddress().getCity().getCountry().setCountryId(presentCountry.getCountryId()),
                () -> user.getAddress().getCity().getCountry().setCountryId(null));

        if(request.getMethod().equals("POST")){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            Token token = securityUtility.generateToken();
            user.setToken(token);
            user.setEnabled(false);

            User createdUser = em.merge(user);

            String link = EmailContentUtil
                    .buildVerificationLink(request.getServerName(), request.getServerPort(), token.getToken());
            String text = EmailContentUtil.buildEmailText(createdUser.getFirstName(), link, EmailType.CONFIRMATION);
            emailService.sendEmail(createdUser.getEmail(), "Confirmation email", text);

            return createdUser;
        }

        return em.merge(user);
    }

    @Override
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException();
        }
        userRepository.deleteById(id);
    }
}
