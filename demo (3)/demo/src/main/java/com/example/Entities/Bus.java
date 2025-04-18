package com.example.Entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity
@Table(name = "buses", uniqueConstraints = @UniqueConstraint(columnNames = "bort_num"))
public class Bus {
    @Id
    @Column
    private long id;
    @Column
    private String brand;
    @Pattern(regexp = "\\d{5}")
    @Column
    private String bort_num;
    @Column
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "routes_of_buses", joinColumns = @JoinColumn(name="route_id"),inverseJoinColumns = @JoinColumn(name = "bus_id"))
    private List<Route>routes;
}
