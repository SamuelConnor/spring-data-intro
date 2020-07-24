package com.example.demo.services;

import com.example.demo.entities.LocationEntity;
import com.example.demo.exceptions.LocationNotFoundException;
import com.example.demo.model.dto.CarDto;
import com.example.demo.model.dto.LocationDto;
import com.example.demo.repositories.LocationEntityRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LocationService {

    @Autowired private LocationEntityRepository locationEntityRepository;
    @PersistenceContext
    EntityManager em;
    ModelMapper modelMapper = new ModelMapper();

    public LocationEntity createLocation(LocationEntity location)
    {
        return locationEntityRepository.save(location);
    }

    public LocationDto findLocationById(Long id)
    {
        if(!locationEntityRepository.findById(id).isPresent())
            throw new LocationNotFoundException();
        LocationDto locationDto = modelMapper.map(locationEntityRepository.findById(id).get(),LocationDto.class);
        locationDto.setCar(locationEntityRepository.findById(id).get().getCarsInLocation());
        return locationDto;
    }


    public List<LocationEntity> findAllLocationsOrdered(Pageable page)
    {

        return locationEntityRepository.findAll(page).getContent();
    }


    public LocationEntity findLocationByIdShort(Long id)
    {
        if(!locationEntityRepository.findById(id).isPresent())
            throw new LocationNotFoundException();
         EntityGraph graph = em.getEntityGraph("location-entity-graph");
         Map<String, Object> properties = new HashMap<>();
         properties.put("javax.persistence.loadgraph", graph);
         LocationEntity location = em.find(LocationEntity.class, id, properties);
         return location;
    }



    public LocationEntity updateLocation(LocationEntity newLocation, Long id)
    {
       LocationEntity location = locationEntityRepository.findById(id).get();
       location.setCountry(newLocation.getCountry());
       return locationEntityRepository.save(location);
    }

    public void deleteLocation(Long id)
    {
        if(!locationEntityRepository.findById(id).isPresent())
            throw new LocationNotFoundException();
        locationEntityRepository.deleteById(id);
    }
}
