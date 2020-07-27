package com.example.demo.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    Date startDate;
    Date endDate;

    @ManyToOne(optional = false)
    public CarEntity car;

    @ManyToOne(optional = false)
    public CustomerEntity customer;
}