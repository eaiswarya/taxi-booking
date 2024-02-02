package com.example.taxibooking.service;


import com.example.taxibooking.constant.Status;
import com.example.taxibooking.contract.request.BookingRequest;
import com.example.taxibooking.contract.response.BookingResponse;
import com.example.taxibooking.model.Booking;
import com.example.taxibooking.repository.BookingRepository;
import com.example.taxibooking.repository.TaxiRepository;
import com.example.taxibooking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class BookingServiceTest {

    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private BookingService bookingService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        bookingRepository = mock(BookingRepository.class);

        modelMapper = mock(ModelMapper.class);
        bookingService = new BookingService(bookingRepository, userRepository, modelMapper);
    }

    @Test
    void testAddBooking() {
        BookingRequest request = new BookingRequest("location1", "location2");
        Booking booking = Booking.builder()
                .pickupLocation(request.getPickupLocation())
                .dropoutLocation(request.getDropoutLocation())
                .bookingTime(LocalDateTime.parse(LocalDateTime.now().toString()))
                .status(Status.BOOKED)
                .build();
        BookingResponse expectedResponse = new BookingResponse(1L, "name", "location1", "location2", LocalDateTime.now(), 10.0, Status.BOOKED);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(modelMapper.map(booking, BookingResponse.class)).thenReturn(expectedResponse);
        BookingResponse actualResponse = bookingService.addBooking(request);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testGetAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        List<BookingResponse> bookingsResponse = new ArrayList<>();

        when(bookingRepository.findAll()).thenReturn(bookings);
        List<BookingResponse> actualResponse = bookingService.getAllBookings();
        assertEquals(bookingsResponse, actualResponse);
        verify(bookingRepository).findAll();

    }

    @Test
    void testGetBooking() {
        Long id = 1L;
        BookingRequest bookingRequest = new BookingRequest("location1", "location2");
        BookingResponse expectedResponse = new BookingResponse(1L, "name", "location1", "location2", LocalDateTime.now(), 100.0, Status.BOOKED);
        Booking booking = new Booking();
        when(modelMapper.map(bookingRequest, Booking.class)).thenReturn(booking);
        when(bookingRepository.findById(id)).thenReturn(Optional.of(booking));
        when(modelMapper.map(booking, BookingResponse.class)).thenReturn(expectedResponse);
        BookingResponse actualResponse = bookingService.getBooking(id);
        assertEquals(expectedResponse, actualResponse);
    }

}



