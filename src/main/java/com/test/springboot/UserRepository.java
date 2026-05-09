package com.test.springboot;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {
    List<User> findByLoginTimeBeforeAndLoggedInFlagTrue(LocalDateTime oneHourAgo);
}
