package com.hmsapp.repository;

import com.hmsapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Additional custom methods can be defined here.
    Optional<User> findByEmail(String email);
    Optional<User> findByMobile(String mobile);
    Optional<User> findByUsername(String username);

}