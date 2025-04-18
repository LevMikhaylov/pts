package com.example.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.orders.Entities.Validation.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
