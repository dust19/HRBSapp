package com.hmsapp.repository;

import com.hmsapp.entity.Property;
import com.hmsapp.entity.PropertyImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PropertyImageRepository extends JpaRepository<PropertyImage, Long> {
    // Additional custom methods can be defined here.
    List<PropertyImage> findByProperty(Property property);
}