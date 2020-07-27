package com.example.demo.services;

import com.example.demo.entities.CarEntity;
import com.example.demo.entities.LocationEntity;
import com.example.demo.exceptions.CarNotFoundException;
import com.example.demo.model.dto.CarDto;
import com.example.demo.repositories.CarEntityRepository;
import com.example.demo.repositories.LocationEntityRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CarService
{
    @Autowired
    private CarEntityRepository carEntityRepository;
    @Autowired
    private LocationEntityRepository locationEntityRepository;

    ModelMapper modelMapper = new ModelMapper();

    public CarEntity createCar(CarEntity carEntity)
    {
        return carEntityRepository.save(carEntity);
    }

    public CarDto findCarById(Long id)
    {
        CarEntity car = carEntityRepository.findById(id).orElse(null);
        return convertToDto(car);
    }

    public Set<CarEntity> findCarsByLocation(String country)
    {
        Iterable<LocationEntity> locationList = locationEntityRepository.findAll();
        LocationEntity loc = null;
        for (LocationEntity location: locationList)
        {
                if (location.getCountry().equals(country))
                    loc=location;
        }
        return loc.getCarsInLocation();
    }

    public List<CarEntity> findAllCars(Pageable page)
    {

        return carEntityRepository.findAll(page).getContent();
    }

    public CarDto updateCar(CarDto carDto, long id)
    {
        CarEntity newCar = convertToEntity(carDto);
        if(carEntityRepository.existsById(id))
        {
            newCar.setId(id);
            return convertToDto(carEntityRepository.save(newCar));
        }
        else
            return null;
    }

    public void deleteCar(Long id)
    {
        CarEntity car = carEntityRepository.findById(id).get();
        if(car == null)
            throw new CarNotFoundException();
        LocationEntity location = car.getLocation();
        Set<CarEntity> carList = location.getCarsInLocation();
        carList.remove(car);
        location.setCar(carList);
        locationEntityRepository.save(location);
        carEntityRepository.deleteById(id);
    }

    private CarDto convertToDto(CarEntity car)
    {
        if(car == null)
            throw new CarNotFoundException();
        CarDto carDto = modelMapper.map(car, CarDto.class);
        return  carDto;
    }

    private CarEntity convertToEntity(CarDto carDto)
    {
        CarEntity car = modelMapper.map(carDto, CarEntity.class);
        return car;
    }

}

