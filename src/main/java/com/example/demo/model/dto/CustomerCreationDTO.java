package com.example.demo.model.dto;

import com.example.demo.entities.CustomerEntity;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CustomerCreationDTO {

    @NotNull
    private String name;

    public CustomerEntity convertDtoToEntity()
    {
        CustomerEntity entity = new CustomerEntity();
        entity.setName(this.name);
        return entity;
    }
}
