package com.example.demo.services;

import com.example.demo.entities.BookingEntity;
import com.example.demo.entities.CustomerEntity;
import com.example.demo.entities.LocationEntity;
import com.example.demo.exceptions.BookingNotFoundException;
import com.example.demo.exceptions.LocationNotFoundException;
import com.example.demo.repositories.BookingEntityRepository;
import com.example.demo.repositories.CustomerEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class BookingService {

    @Autowired private BookingEntityRepository bookingEntityRepository;
    @Autowired private CustomerEntityRepository customerEntityRepository;

    public BookingEntity createBooking(BookingEntity booking) {
        return bookingEntityRepository.save(booking);
    }

    public BookingEntity findBookingById(Long id) {

        if(!bookingEntityRepository.existsById(id)) {
            { throw new BookingNotFoundException(); }
        }
        return bookingEntityRepository.findById(id).get();
    }


    public List<BookingEntity> findAllBookingsOrdered(Pageable page) {
        return bookingEntityRepository.findAll(page).getContent();
    }

    public BookingEntity updateBooking(BookingEntity newBooking, Long id) {
        BookingEntity location = findBookingById(id);
        location.setStartDate(newBooking.getStartDate());
        location.setEndDate(newBooking.getEndDate());
        return bookingEntityRepository.save(location);
    }

    public void deleteBooking(Long id) {
        BookingEntity booking = bookingEntityRepository.findById(id).get();
        if(booking == null)
            throw new BookingNotFoundException();
        CustomerEntity customer = booking.customer;
        Set<BookingEntity> bookingSet = customer.getBookingsByCustomer();
        bookingSet.remove(booking);
        customer.setBookings(bookingSet);
        customerEntityRepository.save(customer);
        bookingEntityRepository.deleteById(id);
    }

    public List<BookingEntity> findAllBookingsToday(Pageable page) {
        List<BookingEntity> allBookingList = bookingEntityRepository.findAll(page).getContent();
        ArrayList<BookingEntity> bookingsToday = new ArrayList<>();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy");
        for (BookingEntity booking: allBookingList) {
            if(formatter.format(date).equals(formatter.format(booking.getEndDate())))
            {
                bookingsToday.add(booking);
            }
        }
        return bookingsToday;
    }
}