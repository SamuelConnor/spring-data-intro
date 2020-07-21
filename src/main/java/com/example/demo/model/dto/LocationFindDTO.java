package com.example.demo.model.dto;

import com.example.demo.entities.LocationEntity;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LocationFindDTO {

    @NotNull
    private String country;

    public LocationFindDTO convertEntityToDto(LocationEntity entity)
    {

        this.country = entity.getCountry();
        return this;
    }
}
