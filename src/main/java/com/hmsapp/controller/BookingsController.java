package com.hmsapp.controller;

import com.hmsapp.entity.Booking;
import com.hmsapp.entity.Property;
import com.hmsapp.repository.PropertyRepository;
import com.hmsapp.repository.RoomAvailabilityRepository;
import com.hmsapp.service.BookingsService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingsController {

    private RoomAvailabilityRepository roomAvailabilityRepository;
    private BookingsService bookingsService;
    private PropertyRepository propertyRepository;

    public BookingsController(RoomAvailabilityRepository roomAvailabilityRepository, BookingsService bookingsService, PropertyRepository propertyRepository) {
        this.roomAvailabilityRepository = roomAvailabilityRepository;
        this.bookingsService = bookingsService;
        this.propertyRepository = propertyRepository;
    }


    //http://localhost:8080/api/v1/bookings/search/rooms?fromDate=2025-01-25&toDate=2025-01-30&roomType=Deluxe&propertyId=1
    @RequestMapping("/search/rooms")
    public ResponseEntity<?> searchRooms(
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate,
            @RequestParam String roomType,
            @RequestParam long propertyId,
            @RequestBody Booking bookings
    ){
        ResponseEntity<?> rooms = bookingsService.searchRooms(fromDate, toDate, roomType, propertyId,bookings);

        return rooms;
    }
}
