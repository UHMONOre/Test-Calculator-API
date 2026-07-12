package com.test.springboot;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);

    Optional<User> findById(Integer id);

    List<User> findByLoginTimeBeforeAndLoggedInFlagTrue(LocalDateTime oneHourAgo);
}
