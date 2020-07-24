package com.example.demo.model.dto;

import com.example.demo.entities.CarEntity;
import com.example.demo.entities.Category;
import com.example.demo.entities.LocationEntity;
import com.example.demo.entities.Transmission;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class CarDto
{
    public Long id;
    String name;
    String registration;
    String manufacturer;
    String model;
    @Enumerated(EnumType.STRING)
    Transmission transmission;
    @Enumerated(EnumType.STRING)
    Category category;
    LocationEntity location;
}



