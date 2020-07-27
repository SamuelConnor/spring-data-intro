package com.example.demo.repositories;

import com.example.demo.entities.CarEntity;
import com.example.demo.entities.LocationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CarEntityRepository extends PagingAndSortingRepository<CarEntity, Long>
{
    Page<CarEntity> findAll(Pageable pageable);
}
