package com.webshoprest.repositories;

import com.webshoprest.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {

    Optional<Country> findByCountryName(String countryName);

}
