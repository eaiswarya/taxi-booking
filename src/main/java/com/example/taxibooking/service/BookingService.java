package com.example.taxibooking.service;

import com.example.taxibooking.constant.Status;
import com.example.taxibooking.contract.request.BookingRequest;
import com.example.taxibooking.contract.response.BookingResponse;
import com.example.taxibooking.contract.response.TaxiResponse;
import com.example.taxibooking.exception.InsufficientBalanceException;
import com.example.taxibooking.model.Booking;
import com.example.taxibooking.model.Taxi;
import com.example.taxibooking.model.User;
import com.example.taxibooking.repository.BookingRepository;
import com.example.taxibooking.repository.TaxiRepository;
import com.example.taxibooking.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final TaxiRepository taxiRepository;
    private final ModelMapper modelMapper;

    public BookingResponse addBooking(Long userId, double distance, BookingRequest request) {
        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

        double minimumCharge = 12.00;
        double fare = distance * minimumCharge;

        if (fare > user.getAccountBalance()) {
            throw new InsufficientBalanceException("Insufficient Balance");
        }

        Booking booking =
                Booking.builder()
                        .pickupLocation(request.getPickupLocation())
                        .dropoutLocation(request.getDropoutLocation())
                        .bookingTime(LocalDateTime.now())
                        .fare(fare)
                        .status(Status.BOOKED)
                        .build();

        User updatedUser =
                User.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .accountBalance(user.getAccountBalance() - booking.getFare())
                        .build();

        userRepository.save(updatedUser);
        bookingRepository.save(booking);

        return modelMapper.map(booking, BookingResponse.class);
    }

    public List<BookingResponse> getAllBookings() {
        List<Booking> bookings = (List<Booking>) bookingRepository.findAll();
        return bookings.stream()
                .map(booking -> modelMapper.map(booking, BookingResponse.class))
                .collect(Collectors.toList());
    }

    public BookingResponse getBooking(Long id) {
        Booking booking =
                bookingRepository
                        .findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Booking not found"));
        return modelMapper.map(booking, BookingResponse.class);
    }

    public void cancelBooking(Long id) {
        Booking booking =
                bookingRepository
                        .findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        if (booking.getStatus() == Status.CANCELLED) {
            throw new IllegalStateException("Booking is already canceled");
        }

        booking.setStatus(Status.CANCELLED);
        bookingRepository.save(booking);
    }

    public List<TaxiResponse> searchTaxi(String pickupLocation) {
        List<Taxi> allTaxies = taxiRepository.findAll();
        List<Taxi> availableTaxies = new ArrayList<>();
        for (Taxi taxies : allTaxies) {
            if (taxies.getCurrentLocation().equals(pickupLocation)) {
                availableTaxies.add(taxies);
            }
        }
        if (availableTaxies.isEmpty()) {
            throw new EntityNotFoundException("Not available");
        } else {
            return availableTaxies.stream()
                    .map(taxi -> modelMapper.map(taxi, TaxiResponse.class))
                    .collect(Collectors.toList());
        }
    }
}
