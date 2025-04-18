package com.example.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Entities.Bus;

public interface BusRepository extends JpaRepository<Bus,Long>{
    void findById(long id);
}
