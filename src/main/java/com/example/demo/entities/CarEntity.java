package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class CarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    String name;
    String registration;
    String manufacturer;
    String model;
    @Enumerated(EnumType.STRING)
    Transmission transmission;
    @Enumerated(EnumType.STRING)
    Category category;

    @ManyToOne(optional = false)
    LocationEntity location;

}

