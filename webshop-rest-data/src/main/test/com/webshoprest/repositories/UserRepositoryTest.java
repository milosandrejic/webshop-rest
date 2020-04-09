package com.webshoprest.repositories;

import com.webshoprest.domain.*;
import com.webshoprest.domain.enums.Roles;
import com.webshoprest.domain.security.Role;
import com.webshoprest.domain.security.Token;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ShoppingCartItemRepository shoppingCartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @BeforeAll
    private void initData() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername("username");
        user.setPassword("password");
        user.setDob(LocalDate.of(2000,2,2));
        user.setEmail("myemail@gmail.com");
        user.setPhoneNumber("1230225133495");
        userRepository.save(user);
    }

    @Test
    void testFindAll() {
        List<User> users = userRepository.findAll();
        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    void testFindById() {
        Optional<User> user = userRepository.findById(1L);
        assertThat(user.isPresent()).isTrue();
        assertThat(user.get().getUserId()).isOne();
    }

    @Test
    void testFindByUsername() {
        Optional<User> user = userRepository.findByUsername("username");
        assertThat(user.isPresent()).isTrue();
        assertThat(user.get().getUsername()).isEqualTo("username");
    }

    @Test
    void testSaveNewUserWithAddress() {
        User user = new User();
        user.setFirstName("Marry");
        user.setLastName("Public");
        user.setUsername("marrypublic");
        user.setPassword("password");
        user.setDob(LocalDate.of(2000,2,2));
        user.setEmail("marrypublic@gmail.com");
        user.setPhoneNumber("1153399151");

        Address address = new Address();
        address.setStreet("High Street");
        address.setStreetNumber(1L);

        City city = new City();
        city.setCityName("New York");

        Country country = new Country();
        country.setCountryName("USA");

        city.setCountry(country);
        address.setCity(city);
        user.setAddress(address);

        User savedUser = userRepository.save(user);

        assertThat(userRepository.count()).isEqualTo(2);
        assertThat(savedUser.getUserId()).isNotNull();
        assertThat(savedUser.getAddress().getStreet()).isEqualTo("High Street");
        assertThat(savedUser.getAddress().getCity().getCityName()).isEqualTo("New York");
        assertThat(savedUser.getAddress().getCity().getCountry().getCountryName()).isEqualTo("USA");
    }

    @Test
    void testSaveNewUserWithTokenAndRole() {
        User user = new User();
        user.setFirstName("Marry");
        user.setLastName("Public");
        user.setUsername("marrypublic");
        user.setPassword("password");
        user.setDob(LocalDate.of(2000,2,2));
        user.setEmail("marrypublic@gmail.com");
        user.setPhoneNumber("1153399151");

        Token token = new Token();
        Role role = new Role();
        role.setRole(Roles.ADMIN);

        user.setToken(token);
        user.setRole(role);

        User savedUser = userRepository.save(user);

        assertThat(user.getToken()).isNotNull();
        assertThat(user.getRole().getRole()).isEqualTo(Roles.ADMIN);
        assertThat(userRepository.count()).isEqualTo(2);
    }

    @Test
    void testUserOrphanRemovalOrders() {
        User user = new User();
        user.setFirstName("Marry");
        user.setLastName("Public");
        user.setUsername("marrypublic");
        user.setPassword("password");
        user.setDob(LocalDate.of(2000,2,2));
        user.setEmail("marrypublic@gmail.com");
        user.setPhoneNumber("1153399151");

        List<Order> orders = new ArrayList<>();
        orders.add(new Order());
        orders.add(new Order());
        orders.add(new Order());
        orders.add(new Order());

        user.setOrders(orders);

        User savedUser = userRepository.save(user);
        assertThat(orderRepository.count()).isEqualTo(4);

        savedUser.getOrders().removeAll(savedUser.getOrders());
        user = userRepository.save(savedUser);

        assertThat(orderRepository.count()).isZero();
        assertThat(user.getOrders()).isEmpty();

    }

    @Test
    void testUpdateUser() {
        Optional<User> user = userRepository.findByUsername("username");
        user.get().setUsername("new username");
        Long id = user.get().getUserId();
        User updatedUser = userRepository.save(user.get());
        assertThat(updatedUser.getUserId()).isEqualTo(id);
        assertThat(updatedUser.getUsername()).isEqualTo("new username");
    }

    @Test
    void testTokenOrphanRemoval() {
        User user = new User();
        user.setFirstName("Marry");
        user.setLastName("Public");
        user.setUsername("marrypublic");
        user.setPassword("password");
        user.setDob(LocalDate.of(1990, 10,10));
        user.setEmail("marrypublic@gmail.com");
        user.setPhoneNumber("1153399151");

        Token token = new Token();
        user.setToken(token);

        User savedUser = userRepository.save(user);
        assertThat(tokenRepository.count()).isOne();


        savedUser.setToken(null);
        user = userRepository.save(savedUser);
        assertThat(tokenRepository.count()).isZero();
    }

    @Test
    void testDeleteById() {
        userRepository.deleteById(1L);
        assertThat(userRepository.count()).isZero();
    }

    @Test
    void testExistsByUsername_true() {
        boolean result = userRepository.existsByUsername("username");
        assertThat(result).isTrue();
    }

    @Test
    void testExistsByUsername_false() {
        boolean result = userRepository.existsByUsername("aaa");
        assertThat(result).isFalse();
    }

    @Test
    void testExistsByEmail_true() {
        boolean result = userRepository.existsByEmail("myemail@gmail.com");
        assertThat(result).isTrue();
    }

    @Test
    void testExistsByEmail_false() {
        boolean result = userRepository.existsByEmail("notexistingemail@gmail.com");
        assertThat(result).isFalse();
    }
}