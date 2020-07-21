package com.example.demo.repositories;

import com.example.demo.entities.LocationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface LocationEntityRepository extends PagingAndSortingRepository<LocationEntity, Long>
{
    Page<LocationEntity> findAll(Pageable pageable);
}
