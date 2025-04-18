package com.example.Entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Entity
@Table(name = "routes")
@Data
public class Route {
    @Id
    @Column
    private long id;
    @Pattern(regexp = "[мc]?\\d+")
    @Column
    private String num;
    @Column
    private String begin_point;
    @Column
    private String end_point;
    @Column
    private List<String>streets;

}
