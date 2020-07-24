package com.example.demo.controllers;

import com.example.demo.entities.CarEntity;
import com.example.demo.exceptions.CarNotFoundException;
import com.example.demo.model.dto.CarDto;
import com.example.demo.repositories.CarEntityRepository;
import com.example.demo.services.CarService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class CarController
{
    @Autowired
    CarService carService;
    @Autowired
    CarEntityRepository carEntityRepository;


    @PostMapping(path = "/car")
    public CarEntity createCar(@RequestBody CarEntity car)
    {
        return carService.createCar(car);
    }

    @GetMapping(path = "/car/{id}")
    public CarDto findCarById(@PathVariable Long id)
    {
        return carService.findCarById(id);
    }

    @GetMapping(path = "/car/findByLocation/{country}")
    public Set<CarEntity> findCarsByLocation(@PathVariable String country)
    {
        return carService.findCarsByLocation(country);
    }

    @GetMapping(path = "/car")
    public List<CarEntity> findAllCars(Pageable page)
    {
        return carService.findAllCars(page);
    }

    @PutMapping(path = "/car/{id}")
    public CarDto updateCar(@RequestBody CarDto carDto, @PathVariable Long id)
    {
        return carService.updateCar(carDto, id);
    }

    @DeleteMapping(path = "/car/{id}")
    public void deleteCar(@PathVariable Long id)
    {
        carService.deleteCar(id);
    }

}
