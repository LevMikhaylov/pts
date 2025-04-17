package com.example.Entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
@Entity
@Table(name="rates")
public class Rate {
    @Id
    @Column
    private long id;
    @Column
    @PositiveOrZero
    private double price;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "route_id")
    private List<Route>routes;
}
