package com.fresco.Visa.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fresco.Visa.Entities.Visa;

@Repository
public interface VisaRepository extends JpaRepository<Visa, Integer> {
    Optional<Visa> findByApplicationId(String applicationId);
}