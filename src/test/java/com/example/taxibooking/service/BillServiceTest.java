package com.example.taxibooking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.taxibooking.contract.request.BillRequest;
import com.example.taxibooking.contract.response.BillResponse;
import com.example.taxibooking.contract.response.UpdateAccountResponse;
import com.example.taxibooking.exception.InsufficientBalanceException;
import com.example.taxibooking.model.Booking;
import com.example.taxibooking.model.User;
import com.example.taxibooking.repository.BookingRepository;
import com.example.taxibooking.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

public class BillServiceTest {
    private BookingRepository bookRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private BillService billService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        bookRepository = mock(BookingRepository.class);
        userRepository = mock(UserRepository.class);
        modelMapper = mock(ModelMapper.class);

        billService = new BillService(bookRepository, userRepository, modelMapper);
    }

    @Test
    void testCalculateFare() {
        BillRequest request = new BillRequest(10D);
        Double minimumCharge = 50.0;

        Booking booking = Booking.builder().fare(request.getDistance() * minimumCharge).build();

        BillResponse expectedResponse = modelMapper.map(booking, BillResponse.class);
        when(bookRepository.save(any(Booking.class))).thenReturn(booking);
        BillResponse actualResponse = billService.calculateFare(request);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testBalanceCheck() {
        Long id = 1L;
        Double distance = 10.0;
        Double fare = 100.0;
        User user =
                User.builder()
                        .id(id)
                        .name("Test User")
                        .email("testuser@example.com")
                        .accountBalance(200.0)
                        .build();
        UpdateAccountResponse expectedResponse =
                new UpdateAccountResponse(id, "Test User", "testuser@example.com", 100.0);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        when(modelMapper.map(any(User.class), eq(UpdateAccountResponse.class)))
                .thenReturn(expectedResponse);

        UpdateAccountResponse actualResponse = billService.balanceCheck(id, distance, fare);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void whenInSufficientBalance_thenThrowsException() {
        Long id = 1L;
        Double accountBalance = 80.0;
        Double fare = 100.0;
        User user =
                User.builder()
                        .id(id)
                        .name("Test User")
                        .accountBalance(accountBalance - fare)
                        .build();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        Exception exception =
                assertThrows(
                        InsufficientBalanceException.class,
                        () -> {
                            billService.balanceCheck(id, accountBalance, fare);
                        });
        String expectedMessage = "Insufficient balance";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testBalanceCheck_UserNotFound() {
        Long id = 1L;
        Double accountBalance = 100.0;
        Double fare = 100.0;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(
                EntityNotFoundException.class,
                () -> billService.balanceCheck(id, accountBalance, fare));
    }

    @Test
    void testFindUserById_UserNotFound() {
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () ->
                        userRepository
                                .findById(id)
                                .orElseThrow(() -> new EntityNotFoundException("User not found")));
    }
}
