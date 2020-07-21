package com.example.demo.controllers;

import com.example.demo.entities.LocationEntity;
import com.example.demo.exceptions.LocationNotFoundException;
import com.example.demo.model.dto.LocationCreationDTO;
import com.example.demo.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;


import java.util.List;

@RestController
@RequestMapping("/api")
public class LocationController {

    @Autowired
    LocationService locationService;

    @PostMapping(path = "/location")
    public LocationEntity createLocation(@RequestBody LocationCreationDTO locationDTO)
    {
        LocationEntity location = locationDTO.convertDtoToEntity();
        return locationService.createLocation(location);
    }

    @ResponseBody
    @GetMapping(path = "/location/{id}")
    public LocationEntity findById(@PathVariable Long id)
    {
        LocationEntity location = locationService.findLocationById(id);
        if(location == null)
            throw new LocationNotFoundException();
        return location;
    }

    @ResponseBody
    @GetMapping(path = "/location")
    public List<LocationEntity> allLocationsOrdered(Pageable page)
    {
        return locationService.findAllLocationsOrdered(page);
    }

    @PutMapping(path = "/location/{id}")
    public ResponseEntity<LocationEntity> updateLocation(@RequestBody LocationEntity location, @PathVariable Long id)
    {
        return ResponseEntity.ok(locationService.updateLocation(location, id));
    }

    @DeleteMapping(path = "/location/{id}")
    public void deleteLocation(@PathVariable Long id)
    {
        if(locationService.findLocationById(id) == null)
            throw new LocationNotFoundException();
        locationService.deleteLocation(id);
    }



}
