package com.example.demo.repositories;

import com.example.demo.entities.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerEntityRepository extends PagingAndSortingRepository<CustomerEntity, Long>
{
    Page<CustomerEntity> findAll(Pageable pageable);
}
