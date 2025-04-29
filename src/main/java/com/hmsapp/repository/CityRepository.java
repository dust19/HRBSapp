package com.hmsapp.repository;

import com.hmsapp.entity.City;
import com.hmsapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findById(String id);
    Optional<City> findByName(String name);

}