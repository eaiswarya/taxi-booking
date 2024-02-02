package com.example.taxibooking.service;

import com.example.taxibooking.constant.Status;
import com.example.taxibooking.contract.request.BookingRequest;
import com.example.taxibooking.contract.response.BookingResponse;
import com.example.taxibooking.contract.response.CancelResponse;
import com.example.taxibooking.model.Booking;
import com.example.taxibooking.model.User;
import com.example.taxibooking.repository.BookingRepository;
import com.example.taxibooking.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    public BookingResponse addBooking(BookingRequest request) {
        Booking booking = Booking.builder()
                .pickupLocation(request.getPickupLocation())
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

    public BookingResponse getBooking(long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));
        return modelMapper.map(booking, BookingResponse.class);
    }
    public String cancelBookingById(Long id){
        Booking booking=bookingRepository.findById(id).orElseThrow(()->new RuntimeException("Booking not found"));

        Booking newBooking=Booking.builder()
                .status(Status.valueOf("CANCELLED"))
                .build();

        bookingRepository.save(newBooking);
        return "Booked taxi has been cancelled with"+id+"successfully";
    }


    public BookingResponse calculateFare(long userId, double distance, BookingRequest request) {

        double minimumCharge = 10.0;
        double fare = distance * minimumCharge;

        Booking booking = Booking.builder()
                .pickupLocation(request.getPickupLocation())
                .dropoutLocation(request.getDropoutLocation())
                .bookingTime(LocalDateTime.now())
                .fare(fare)
                .status(Status.BOOKED)
                .build();

        booking = bookingRepository.save(booking);
        return modelMapper.map(booking, BookingResponse.class);
    }
}



