package com.hmsapp.repository;

import com.hmsapp.entity.Property;
import com.hmsapp.entity.Reviews;
import com.hmsapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewsRepository extends JpaRepository<Reviews, Long> {

    Reviews findByPropertyAndUser (Property property, User user);
    List<Reviews> findByUser(User user);

    List<Reviews> findByProperty(Property property);
}