package com.test.springboot;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CalculationsRepository extends JpaRepository<Calculations,Integer> {
}
