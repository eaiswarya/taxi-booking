package com.example.taxibooking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import jakarta.persistence.EntityNotFoundException;
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

        Long userId = 1L;
        double distance = 1.0;
        BookingRequest request = new BookingRequest("location1", "location2");
        User user = new User(1L, "Name", "name@email.com", "password", 100.0);
        Taxi taxi = new Taxi(1L, "Name", "ABC123", "location1");
        Booking booking = new Booking(1L,null,null,100.0 ,null,10.0,Status.BOOKED,user,taxi);
        BookingResponse expectedResponse = new BookingResponse(1L,"location","location2",null, Status.BOOKED);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(any(Booking.class), eq(BookingResponse.class))).thenReturn(expectedResponse);

        BookingResponse actualResponse = bookingService.addBooking(userId, distance, request);

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
        Booking booking = new Booking(1L,"JD","JDSJ",100.0,null,50.0,null,null,null);
        BookingResponse expectedResponse = modelMapper.map(booking, BookingResponse.class);

        when(bookingRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> bookingService.getBooking(id));

        when(bookingRepository.findById(id)).thenReturn(Optional.of(booking));

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
                        1L, "location", "location2",null, Status.BOOKED);
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
