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
import com.example.taxibooking.repository.BookingRepository;
import com.example.taxibooking.repository.TaxiRepository;
import com.example.taxibooking.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

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
                        1L,
                        "location1",
                        "location2",
                        LocalDateTime.now().toString(),
                        100.0,
                        Status.BOOKED);
        Booking booking = new Booking();
        when(modelMapper.map(bookingRequest, Booking.class)).thenReturn(booking);
        when(bookingRepository.findById(id)).thenReturn(Optional.of(booking));
        when(modelMapper.map(booking, BookingResponse.class)).thenReturn(expectedResponse);
        BookingResponse actualResponse = bookingService.getBooking(id);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testSearchTaxi() {
        Taxi taxi1 = new Taxi(1L, "sharok", "KL 03 5678", "cvm");
        Taxi taxi2 = new Taxi(1L, "midhun", "KL 05 7465", "cvm");

        List<Taxi> availableTaxies = Arrays.asList(taxi1, taxi2);
        when(taxiRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(EntityNotFoundException.class, () -> bookingService.searchTaxi("cvm"));
        when(taxiRepository.findAll()).thenReturn(availableTaxies);

        List<TaxiResponse> expectedResponse =
                availableTaxies.stream()
                        .map(taxi -> modelMapper.map(taxi, TaxiResponse.class))
                        .collect(Collectors.toList());

        List<TaxiResponse> actualResponse = bookingService.searchTaxi("cvm");
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testGetBooking_UserNotFound() {
        Long id = 1L;
        BookingResponse request =
                new BookingResponse(
                        1L,
                        "location",
                        "location2",
                        LocalDateTime.now().toString(),
                        100.0,
                        Status.BOOKED);
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
}
