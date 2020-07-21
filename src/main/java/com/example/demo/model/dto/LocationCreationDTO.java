package com.example.demo.model.dto;

import com.example.demo.entities.LocationEntity;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LocationCreationDTO {

    @NotNull
    private String country;

    public LocationEntity convertDtoToEntity()
    {
        LocationEntity entity = new LocationEntity();
        entity.setCountry(this.country);
        return entity;
    }
}
