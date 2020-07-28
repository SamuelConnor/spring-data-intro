package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
public class CarEntity {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Getter
    @Setter
    String name;

    @Getter
    @Setter
    String registration;

    @Getter
    @Setter
    String manufacturer;

    @Getter
    @Setter
    String model;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    Transmission transmission;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    Category category;

    @Getter
    @Setter
    @ManyToOne(optional = false)
    LocationEntity location;

    @Setter
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "car", fetch = FetchType.LAZY)
    Set<BookingEntity> bookings;


    @JsonIgnore
    public Set<BookingEntity> getBookings(){
        return bookings;
    }

}

