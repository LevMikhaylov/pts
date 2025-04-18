package com.example.Services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.example.orders.Entities.Validation.User;
import com.example.orders.Repositories.UserRepository;

import jakarta.validation.Valid;
@Validated
@Service
public class UserService {
    @Autowired
    private UserRepository ur;
    public User addUser(@Valid User user){
        user.setRegistrationDate(LocalDate.now());
        return ur.save(user);
    }
    public List<User> getAllUsers(){
        return ur.findAll();
    }
}
