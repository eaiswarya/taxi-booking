package com.example.taxibooking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

public class BookingServiceTest {

    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private TaxiRepository taxiRepository;
    private ModelMapper modelMapper;
    private BookingService bookingService;
    @InjectMocks
    private TaxiService taxiService;



    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        bookingRepository = mock(BookingRepository.class);
        userRepository = mock(UserRepository.class);
        taxiRepository = mock(TaxiRepository.class);
        modelMapper = mock(ModelMapper.class);

        bookingService =
                new BookingService(bookingRepository, userRepository, taxiRepository, modelMapper);
    }

    @Test
    void testAddBooking() {
        BookingRequest bookingRequest = new BookingRequest("location1", "location2");
        Booking booking =
                Booking.builder()
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
        BookingResponse expectedResponse =
                new BookingResponse(
                        1L, "location1", "location2", LocalDateTime.now(), 100.0, Status.BOOKED);
        Booking booking = new Booking();
        when(modelMapper.map(bookingRequest, Booking.class)).thenReturn(booking);
        when(bookingRepository.findById(id)).thenReturn(Optional.of(booking));
        when(modelMapper.map(booking, BookingResponse.class)).thenReturn(expectedResponse);
        BookingResponse actualResponse = bookingService.getBooking(id);
        assertEquals(expectedResponse, actualResponse);
    }
    @Test
    void testSearchTaxi() {
        Long id = 1L;
        String pickupLocation = "location1";
        User user = new User();
        Taxi taxi1 = new Taxi();
        taxi1.setCurrentLocation(pickupLocation);
        Taxi taxi2 = new Taxi();
        taxi2.setCurrentLocation("location2");
        List<Taxi> allTaxies = Arrays.asList(taxi1, taxi2);
        List<Taxi> availableTaxies = Arrays.asList(taxi1);
        TaxiResponse expectedResponse = new TaxiResponse();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(taxiRepository.findAll()).thenReturn(allTaxies);
        when(modelMapper.map(taxi1, TaxiResponse.class)).thenReturn(expectedResponse);

        List<TaxiResponse> actualResponse = bookingService.searchTaxi(id, pickupLocation);

        assertEquals(1, actualResponse.size());
        assertEquals(expectedResponse, actualResponse.get(0));
    }

    @Test
    void testGetBooking_UserNotFound() {
        Long id = 1L;
        BookingResponse request = new BookingResponse(1L,"location","location2",LocalDateTime.now(),100.0,Status.BOOKED);
        when(bookingRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> bookingService.getBooking(id));
    }
    @Test
    void testCancelBookingNotFound() {
        Long id = 1L;
        when(bookingRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> bookingService.cancelBooking(id));
        verify(bookingRepository, never()).save(any());
    }
    @Test
    void testSearchTaxi_UserNotFound() {
        Long id = 1L;
        String pickupLocation="location";
        when(bookingRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> bookingService.searchTaxi(id,pickupLocation));

    }

}