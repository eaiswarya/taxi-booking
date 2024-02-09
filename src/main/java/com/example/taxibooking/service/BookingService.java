package com.example.taxibooking.service;

import com.example.taxibooking.constant.Status;
import com.example.taxibooking.contract.request.BookingRequest;
import com.example.taxibooking.contract.response.BookingResponse;
import com.example.taxibooking.exception.BookingNotFoundException;
import com.example.taxibooking.exception.CancellationFailedException;
import com.example.taxibooking.exception.InsufficientBalanceException;
import com.example.taxibooking.model.Booking;
import com.example.taxibooking.model.Taxi;
import com.example.taxibooking.model.User;
import com.example.taxibooking.repository.BookingRepository;
import com.example.taxibooking.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final TaxiService taxiService;
    private final ModelMapper modelMapper;

    public BookingResponse addBooking(Long user_id, double distance, BookingRequest request) {
        User user = userRepository.findById(user_id).orElseThrow(EntityNotFoundException::new);

        List<Taxi> availableTaxi = taxiService.findAvailableTaxis(request.getPickupLocation());
        Taxi nearestTaxi = availableTaxi.get(0);

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
                        .distance(distance)
                        .status(Status.BOOKED)
                        .user(user)
                        .taxi(nearestTaxi)
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

    public String cancelBooking(Long id) {
        Optional<Booking> optionalBooking = bookingRepository.findById(id);
        if (optionalBooking.isEmpty()) {
            throw new BookingNotFoundException("Booking Not Found");
        }
        bookingRepository.deleteById(id);
        if (bookingRepository.existsById(id)) {
            throw new CancellationFailedException("Cancellation Failure");
        } else {
            return "Successfully cancelled";
        }
    }
}
