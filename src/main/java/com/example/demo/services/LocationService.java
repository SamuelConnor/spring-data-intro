package com.example.demo.services;

import com.example.demo.entities.LocationEntity;
import com.example.demo.repositories.LocationEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    @Autowired private LocationEntityRepository locationEntityRepository;

    public LocationEntity createLocation(LocationEntity location)
    {
        return locationEntityRepository.save(location);
    }

    public LocationEntity findLocationById(Long Id)
    {
        return locationEntityRepository.findById(Id).orElse(null);
    }


    public List<LocationEntity> findAllLocationsOrdered(Pageable page)
    {
        return locationEntityRepository.findAll(page).getContent();
    }

    public LocationEntity updateLocation(LocationEntity newLocation, Long id)
    {
       LocationEntity location = findLocationById(id);
       location.setCountry(newLocation.getCountry());
       return locationEntityRepository.save(location);
    }

    public void deleteLocation(Long id)
    {
        locationEntityRepository.deleteById(id);
    }
}
