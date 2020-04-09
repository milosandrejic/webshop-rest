package com.webshoprest.repositories;

import com.webshoprest.domain.City;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CityRepositoryTest {

    @Autowired
    private CityRepository cityRepository;

    @Test
    void testFindByCityName() {
        City city = new City();
        city.setCityName("Belgrade");

        City savedCity = cityRepository.save(city);

        Optional<City> fromDbCity = cityRepository.findByCityName("Belgrade");

        assertThat(fromDbCity.isPresent()).isTrue();
        assertThat(fromDbCity.get().getCityName()).isEqualTo("Belgrade");
    }

    @Test
    void testFindByCityName_notFound() {

        Optional<City> fromDbCity = cityRepository.findByCityName("Belgrade");

        assertThat(fromDbCity.isPresent()).isFalse();
    }
}