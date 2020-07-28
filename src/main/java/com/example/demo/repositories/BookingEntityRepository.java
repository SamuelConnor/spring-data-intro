package com.example.demo.repositories;

import com.example.demo.entities.BookingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BookingEntityRepository extends PagingAndSortingRepository<BookingEntity, Long>
{
    Page<BookingEntity> findAll(Pageable pageable);
}
