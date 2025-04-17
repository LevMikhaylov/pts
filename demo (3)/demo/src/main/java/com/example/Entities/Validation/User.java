package com.example.Entities.Validation;

import java.time.LocalDate;

import javax.validation.constraints.Pattern;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
@Entity
public class User {
    @Id
    @Column
    private Long id;
    @Size(min = 2)
    @Column
    private String name;
    @Size(min = 2)
    @Column
    private String surname;
    @Column
    private String patr;
    @Email
    @Column
    private String email;
    @Pattern(regexp="\\+7\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}$", message="Invalid phone number format")//Задаём формат номера телефона в России
    private String phoneNumber;
    @Size(min=6)
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$")
    @Column
    private String password;
    @Column
    private LocalDate registrationDate; 
}
