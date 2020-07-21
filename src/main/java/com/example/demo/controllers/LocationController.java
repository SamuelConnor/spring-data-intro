package com.example.demo.controllers;

import com.example.demo.entities.LocationEntity;
import com.example.demo.model.dto.LocationCreationDTO;
import com.example.demo.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LocationController {

    @Autowired
    LocationService locationService;

    @PostMapping(path = "/location")
    public LocationEntity createLocation(@RequestBody LocationCreationDTO locationDTO)
    {
        //include validation possibly.
        LocationEntity location = locationDTO.convertDtoToEntity();
        return locationService.createLocation(location);
    }

    @ResponseBody
    @GetMapping(path = "/location/{id}")
    public LocationEntity findById(@PathVariable Long id)
    {
        LocationEntity location = locationService.findLocationById(id);
        if(location == null)
            throw new locationNotFoundException();
        return location;
    }

    @ResponseBody
    @GetMapping(path = "/location")
    public List<LocationEntity> allLocationsOrdered(@RequestParam (value = "page", defaultValue = "0") int page,
                                                    @RequestParam (value = "size", defaultValue = "10") int size,
                                                    @RequestParam (value = "criteria", defaultValue = "id") String criteria,
                                                    @RequestParam (value = "ascending", defaultValue = "true") boolean ascending)
    {
        return locationService.findAllLocationsOrdered(page, size, criteria, ascending);
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
            throw new locationNotFoundException();
        locationService.deleteLocation(id);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No location with that id")
    public class locationNotFoundException extends RuntimeException {}

}
