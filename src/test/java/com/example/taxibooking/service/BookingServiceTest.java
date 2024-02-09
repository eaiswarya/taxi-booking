package com.example.taxibooking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.taxibooking.constant.Status;
import com.example.taxibooking.contract.request.BookingRequest;
import com.example.taxibooking.contract.response.BookingResponse;
import com.example.taxibooking.exception.BookingNotFoundException;
import com.example.taxibooking.exception.EntityAlreadyExistsException;
import com.example.taxibooking.model.Booking;
import com.example.taxibooking.model.Taxi;
import com.example.taxibooking.model.User;
import com.example.taxibooking.repository.BookingRepository;
import com.example.taxibooking.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

public class BookingServiceTest {

    @Mock private BookingRepository bookingRepository;
    @Mock private UserRepository userRepository;
    @Mock private ModelMapper modelMapper;
    @InjectMocks private BookingService bookingService;
    @Mock private TaxiService taxiService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddBooking3() {
        when(bookingRepository.save(Mockito.<Booking>any())).thenReturn(new Booking());
        when(userRepository.save(Mockito.<User>any())).thenReturn(new User());
        User buildResult =
                User.builder()
                        .accountBalance(120.0d)
                        .email("name@gmail.com")
                        .id(1L)
                        .name("name")
                        .password("password")
                        .build();
        Optional<User> ofResult = Optional.of(buildResult);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        ArrayList<Taxi> taxiList = new ArrayList<>();
        taxiList.add(new Taxi());
        when(taxiService.findAvailableTaxis(Mockito.<String>any())).thenReturn(taxiList);
        BookingResponse bookingResponse = new BookingResponse();
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<BookingResponse>>any()))
                .thenReturn(bookingResponse);
        BookingResponse actualAddBookingResult =
                bookingService.addBooking(
                        1L, 10.0d, new BookingRequest("Pickup Location", "Dropout Location"));
        verify(taxiService).findAvailableTaxis(Mockito.<String>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<BookingResponse>>any());
        verify(userRepository).findById(Mockito.<Long>any());
        verify(bookingRepository).save(Mockito.<Booking>any());
        verify(userRepository).save(Mockito.<User>any());
        assertSame(bookingResponse, actualAddBookingResult);
    }

    @Test
    public void testEntityAlreadyExistsException() {
        String entity = "User";
        EntityAlreadyExistsException exception =
                assertThrows(
                        EntityAlreadyExistsException.class,
                        () -> {
                            throw new EntityAlreadyExistsException(entity);
                        });

        assertEquals(entity, exception.getEntity());
        assertEquals(0L, exception.getId());
        assertEquals(entity, exception.getMessage());
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
        Booking booking = new Booking(1L, "JD", "JDSJ", 100.0, null, 50.0, null, null, null);
        BookingResponse expectedResponse = modelMapper.map(booking, BookingResponse.class);

        when(bookingRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> bookingService.getBooking(id));

        when(bookingRepository.findById(id)).thenReturn(Optional.of(booking));

        BookingResponse actualResponse = bookingService.getBooking(id);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testCancelBooking() {
        Long id = 1L;
        Booking booking =
                Booking.builder()
                        .id(1L)
                        .pickupLocation("Pala")
                        .dropoutLocation("Kollam")
                        .bookingTime(LocalDateTime.now())
                        .status(Status.BOOKED)
                        .build();
        when(bookingRepository.findById(id)).thenReturn(Optional.of(booking));
        doNothing().when(bookingRepository).deleteById(id);
        when(bookingRepository.existsById(id)).thenReturn(false);
        String actualResult = bookingService.cancelBooking(id);
        assertEquals("Successfully cancelled", actualResult);
    }

    @Test
    public void testUpdateBooking_BookingNotFound() {
        Long id = 1L;

        when(bookingRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                BookingNotFoundException.class,
                () -> {
                    bookingService.cancelBooking(id);
                });
    }

    @Test
    void testGetBooking_UserNotFound() {
        Long id = 1L;
        BookingResponse request =
                new BookingResponse(1L, "location", "location2", 10.0, 10.0, Status.BOOKED);
        when(bookingRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> bookingService.getBooking(id));
    }

    @Test
    void testCancelBookingNotFound() {
        Long id = 1L;
        when(bookingRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(BookingNotFoundException.class, () -> bookingService.cancelBooking(id));
        verify(bookingRepository, never()).save(any());
    }
}
