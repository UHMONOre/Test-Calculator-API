package com.test.springboot;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CalculationsRepository extends JpaRepository<Calculations,Integer> {
    Optional<Calculations> findById(Integer id);
}
