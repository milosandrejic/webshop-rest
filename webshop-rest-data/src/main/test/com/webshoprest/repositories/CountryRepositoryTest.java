package com.webshoprest.repositories;

import com.webshoprest.domain.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CountryRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;

    @Test
    void testFindByCountryName() {
        Country country = new Country();
        country.setCountryName("Serbia");

        country = countryRepository.save(country);

        Optional<Country> foundCountry = countryRepository.findByCountryName("Serbia");
        assertThat(foundCountry.isPresent()).isTrue();
        assertThat(foundCountry.get().getCountryName()).isEqualTo("Serbia");
    }

    @Test
    void testFindByCountryName_notFound() {
        Optional<Country> foundCountry = countryRepository.findByCountryName("Serbia");
        assertThat(foundCountry.isPresent()).isFalse();
    }
}