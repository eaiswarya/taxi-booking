package com.example.taxibooking.service;

import com.example.taxibooking.constant.Status;
import com.example.taxibooking.contract.request.BookingRequest;
import com.example.taxibooking.contract.response.BookingResponse;
import com.example.taxibooking.model.Booking;
import com.example.taxibooking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ModelMapper modelMapper;

    public BookingResponse addBooking(BookingRequest request) {
        Booking booking = Booking.builder().pickupLocation(request.getPickupLocation())
                .dropoutLocation(request.getDropoutLocation())
                .bookingTime(LocalDateTime.parse(LocalDateTime.now().toString()))
                .status(Status.BOOKED)
                .build();
        booking = bookingRepository.save(booking);
        return modelMapper.map(booking, BookingResponse.class);
    }

    public List<BookingResponse> getAllBookings() {
        List<Booking> bookings = (List<Booking>) bookingRepository.findAll();
        return bookings.stream().map(booking -> modelMapper.map(booking, BookingResponse.class))
                .collect(Collectors.toList());

    }
    public BookingResponse cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id).get();
        booking.setStatus(Status.CANCELLED);
        booking = bookingRepository.save(booking);
        return modelMapper.map(booking, BookingResponse.class);
    }

}
