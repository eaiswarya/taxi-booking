package com.example.taxibooking.service;


import com.example.taxibooking.constant.Status;
import com.example.taxibooking.contract.request.BookingRequest;
import com.example.taxibooking.contract.response.BookingResponse;
import com.example.taxibooking.contract.response.TaxiResponse;
import com.example.taxibooking.model.Booking;
import com.example.taxibooking.model.Taxi;
import com.example.taxibooking.model.User;
import com.example.taxibooking.repository.BookingRepository;
import com.example.taxibooking.repository.TaxiRepository;
import com.example.taxibooking.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class BookingServiceTest {

    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private TaxiRepository taxiRepository;
    private ModelMapper modelMapper;
        private BookingService bookingService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        bookingRepository = mock(BookingRepository.class);
        userRepository = mock(UserRepository.class);
        taxiRepository= mock(TaxiRepository.class);
        modelMapper=mock(ModelMapper.class);

        bookingService = new BookingService(bookingRepository,userRepository,taxiRepository,modelMapper);
    }

    @Test
    void testAddBooking() {
        BookingRequest bookingRequest = new BookingRequest("location1","location2");
        Booking booking = Booking.builder()
                .pickupLocation(bookingRequest.getPickupLocation())
                .dropoutLocation(bookingRequest.getDropoutLocation())
                .bookingTime(LocalDateTime.parse(LocalDateTime.now().toString()))
                .status(Status.BOOKED)
                .build();


        BookingResponse expectedResponse = new BookingResponse();
        when(bookingRepository.save(any())).thenReturn(booking);

        when(modelMapper.map(booking, BookingResponse.class)).thenReturn(expectedResponse);

        BookingResponse actualResponse = bookingService.addBooking(bookingRequest);

        assertEquals(expectedResponse, actualResponse);

}

    @Test
    void testGetAllBookings() {
        List<Booking> booking = new ArrayList<>();
        List<BookingResponse> bookingsResponse = new ArrayList<>();

        when(bookingRepository.findAll()).thenReturn(booking);
        List<BookingResponse> actualResponse = bookingService.getAllBookings();
        assertEquals(bookingsResponse, actualResponse);
        verify(bookingRepository).findAll();

    }
    @Test
    void testGetBooking() {
        Long id = 1L;
        BookingRequest bookingRequest = new BookingRequest("location1", "location2");
        BookingResponse expectedResponse = new BookingResponse(1L, "location1", "location2", LocalDateTime.now(), 100.0, Status.BOOKED);
        Booking booking = new Booking();
        when(modelMapper.map(bookingRequest, Booking.class)).thenReturn(booking);
        when(bookingRepository.findById(id)).thenReturn(Optional.of(booking));
        when(modelMapper.map(booking, BookingResponse.class)).thenReturn(expectedResponse);
        BookingResponse actualResponse = bookingService.getBooking(id);
        assertEquals(expectedResponse, actualResponse);
    }
    @Test
    void testSearchTaxi() {
        Long userId = 1L;
        String pickupLocation = "Test Location";
        User mockUser = new User();
        List<Taxi> allTaxis = new ArrayList<>();
        List<TaxiResponse> expectedResponses = new ArrayList<>();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        when(taxiRepository.findAll()).thenReturn(allTaxis);

        for (Taxi taxi : allTaxis) {
            if (taxi.getCurrentLocation().equals(pickupLocation)) {
                TaxiResponse mockResponse = new TaxiResponse();
                when(modelMapper.map(taxi, TaxiResponse.class)).thenReturn(mockResponse);
                expectedResponses.add(mockResponse);
            }
        }

        List<TaxiResponse> actualResponses = bookingService.searchTaxi(userId, pickupLocation);

        assertEquals(expectedResponses, actualResponses);
    }
    @Test
    void testCalculateFare() {

        Long userId = 1L;
        double distance = 10.0;
        BookingRequest request = new BookingRequest();
        User mockUser = new User();

        double minimumCharge = 10.0;
        double expectedFare = distance * minimumCharge;

        Booking expectedBooking = Booking.builder()
                .pickupLocation(request.getPickupLocation())
                .dropoutLocation(request.getDropoutLocation())
                .bookingTime(LocalDateTime.now())
                .fare(expectedFare)
                .status(Status.BOOKED)
                .build();

        BookingResponse expectedResponse = new BookingResponse();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        when(bookingRepository.save(any())).thenReturn(expectedBooking);

        when(modelMapper.map(expectedBooking, BookingResponse.class)).thenReturn(expectedResponse);

        BookingResponse actualResponse = bookingService.calculateFare(userId, distance, request);

        assertEquals(expectedResponse, actualResponse);
    }



}



