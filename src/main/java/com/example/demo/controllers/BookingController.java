package com.example.demo.controllers;

import com.example.demo.entities.BookingEntity;
import com.example.demo.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookingController {

    @Autowired BookingService bookingService;

    @PostMapping(path = "/booking")
    public BookingEntity createBooking(@RequestBody BookingEntity booking)
    {
        return bookingService.createBooking(booking);
    }

    @ResponseBody
    @GetMapping(path = "/booking/{id}")
    public BookingEntity findById(@PathVariable Long id)
    {
        return bookingService.findBookingById(id);
    }

    @ResponseBody
    @GetMapping(path = "/booking")
    public List<BookingEntity> allBookingsOrdered(Pageable page)
    {
        return bookingService.findAllBookingsOrdered(page);
    }

    @ResponseBody
    @GetMapping(path = "/booking/today")
    public List<BookingEntity> allBookingsToday(Pageable page)
    {
        return bookingService.findAllBookingsToday(page);
    }


    @PutMapping(path = "/booking/{id}")
    public ResponseEntity<BookingEntity> updateBooking(@RequestBody BookingEntity booking, @PathVariable Long id)
    {
        return ResponseEntity.ok(bookingService.updateBooking(booking, id));
    }

    @DeleteMapping(path = "/booking/{id}")
    public void deleteBooking(@PathVariable Long id)
    {
        bookingService.deleteBooking(id);
    }

}