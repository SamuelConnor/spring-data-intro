package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Setter;
import lombok.Getter;

import javax.persistence.*;
import java.util.Set;



@Entity
@NamedEntityGraph(name = "location-entity-graph", attributeNodes = {@NamedAttributeNode("id"), @NamedAttributeNode("country")})
@NamedEntityGraph(name = "location-entity-graph-long", attributeNodes = {@NamedAttributeNode("id"), @NamedAttributeNode("country"), @NamedAttributeNode("car")})

public class LocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    public Long id;

    @Getter
    @Setter
    private String country;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "location", fetch = FetchType.LAZY)
    @Setter
    private Set<CarEntity> car;


    @JsonIgnore
    public Set<CarEntity> getCarsInLocation(){
        return car;
    }
}
