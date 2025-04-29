package com.hmsapp.service;

import com.hmsapp.entity.Booking;
import com.hmsapp.entity.Property;
import com.hmsapp.entity.RoomAvailability;
import com.hmsapp.repository.BookingRepository;
import com.hmsapp.repository.PropertyRepository;
import com.hmsapp.repository.RoomAvailabilityRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingsService {
    private RoomAvailabilityRepository roomAvailabilityRepository;
    private ModelMapper modelMapper;
    private PropertyRepository propertyRepository;
    private BookingRepository bookingRepository;
    private PDFGenerator PDFGenerator;
    private TwilioService twilioService;

    public BookingsService(RoomAvailabilityRepository roomAvailabilityRepository, ModelMapper modelMapper, PropertyRepository propertyRepository, BookingRepository bookingRepository, PDFGenerator PDFGenerator, TwilioService twilioService) {
        this.roomAvailabilityRepository = roomAvailabilityRepository;
        this.modelMapper = modelMapper;
        this.propertyRepository = propertyRepository;
        this.bookingRepository = bookingRepository;
        this.PDFGenerator = PDFGenerator;
        this.twilioService = twilioService;
    }


    //http://localhost:8080/api/v1/bookings/search/rooms?fromDate=2025-01-25&toDate=2025-01-30&roomType=Deluxe&propertyId=1
    public ResponseEntity<?> searchRooms(LocalDate fromDate, LocalDate toDate, String roomType, long propertyId, Booking bookings) {
        LocalDate today = LocalDate.now();

        // Validate dates: Ensure they are today or in the future
        if (fromDate.isBefore(today) || toDate.isBefore(today)) {
            return new ResponseEntity<>("Booking dates must be today or a future date!", HttpStatus.BAD_REQUEST);
        }

        // Validate that fromDate is before toDate
        if (fromDate.isAfter(toDate)) {
            return new ResponseEntity<>("From date must be before or equal to To date!", HttpStatus.BAD_REQUEST);
        }

        List<RoomAvailability> rooms = roomAvailabilityRepository.findAvailableRooms(propertyId, roomType, fromDate, toDate);

        Property property = propertyRepository.findById(propertyId).orElse(null);
        if (property == null) {
            return new ResponseEntity<>("Property not found!", HttpStatus.NOT_FOUND);
        }

        for (RoomAvailability roomAvailability : rooms) {
            if (roomAvailability.getTotalRooms() == 0) {
                return new ResponseEntity<>("No rooms available for this date!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        bookings.setProperty(property);
        Booking savedBookings = bookingRepository.save(bookings);
        PDFGenerator.generatePdf("E:\\bookings_docs\\sample.pdf", savedBookings);

        twilioService.sendSms("+917899609771", "Test");
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

}
