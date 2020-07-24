package com.example.demo.controllers;

import com.example.demo.entities.LocationEntity;
import com.example.demo.exceptions.LocationNotFoundException;
import com.example.demo.model.dto.CarDto;
import com.example.demo.model.dto.LocationCreationDTO;
import com.example.demo.model.dto.LocationDto;
import com.example.demo.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;


import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LocationController {

    @Autowired
    LocationService locationService;

    @PostMapping(path = "/location")
    public LocationEntity createLocation(@RequestBody LocationCreationDTO locationDTO)
    {
        return locationService.createLocation(locationDTO.convertDtoToEntity());
    }

    @ResponseBody
    @GetMapping(path = "/location/{id}")
    public LocationDto findById(@PathVariable Long id)
    {
        return locationService.findLocationById(id);
    }

    @ResponseBody
    @GetMapping(path = "/location")
    public List<LocationEntity> allLocationsOrdered(Pageable page)
    {
        return locationService.findAllLocationsOrdered(page);
    }

    @GetMapping(path = "/location/findByIdShort/{id}")
    public LocationEntity findLocationByIdShort(@PathVariable Long id)
    {
        return locationService.findLocationByIdShort(id);
    }

    @PutMapping(path = "/location/{id}")
    public ResponseEntity<LocationEntity> updateLocation(@RequestBody LocationEntity location, @PathVariable Long id)
    {
        return ResponseEntity.ok(locationService.updateLocation(location, id));
    }

    @DeleteMapping(path = "/location/{id}")
    public void deleteLocation(@PathVariable Long id)
    {
        locationService.deleteLocation(id);
    }



}
