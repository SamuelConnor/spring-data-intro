package com.example.demo.model.dto;

import com.example.demo.entities.CarEntity;
import com.example.demo.entities.Category;
import com.example.demo.entities.LocationEntity;
import com.example.demo.entities.Transmission;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class LocationDto
{
    public Long id;
    String country;
    Set<CarEntity> car;
}



