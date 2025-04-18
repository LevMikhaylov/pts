package com.example.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Entities.Rate;

public interface RateRepository extends JpaRepository<Rate, Long>{
    void findById(long id);
}
