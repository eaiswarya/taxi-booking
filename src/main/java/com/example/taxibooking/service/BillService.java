package com.example.taxibooking.service;

import com.example.taxibooking.contract.request.BillRequest;
import com.example.taxibooking.contract.response.BillResponse;
import com.example.taxibooking.contract.response.UpdateAccountResponse;
import com.example.taxibooking.exception.InsufficientBalanceException;
import com.example.taxibooking.model.Booking;
import com.example.taxibooking.model.User;
import com.example.taxibooking.repository.BookingRepository;
import com.example.taxibooking.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BillService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public BillResponse calculateFare(BillRequest request) {
        Double minimumCharge = 10.0;
        Booking booking = Booking.builder().fare(request.getDistance() * minimumCharge).build();
        bookingRepository.save(booking);
        return modelMapper.map(booking, BillResponse.class);
    }

    public UpdateAccountResponse balanceCheck(Long id, Double accountBalance, Double fare) {
        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("user not found"));
        Double updatedBalance = user.getAccountBalance() + accountBalance - fare;
        if (updatedBalance < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
        user =
                User.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .accountBalance(updatedBalance)
                        .build();
        user = userRepository.save(user);
        return modelMapper.map(user, UpdateAccountResponse.class);
    }
}
