package com.example.taxibooking.controller;

import com.example.taxibooking.contract.request.BookingRequest;
import com.example.taxibooking.contract.response.BookingResponse;
import com.example.taxibooking.service.BookingService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/add")
    public @ResponseBody BookingResponse addBooking(@RequestBody BookingRequest request) {
        return bookingService.addBooking(request);
    }

    @GetMapping("/details")
    public @ResponseBody List<BookingResponse> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/details/{id}")
    public BookingResponse getBooking(@PathVariable Long id) {
        return bookingService.getBooking(id);

    }
    @DeleteMapping("/cancel/{id}")
    public void cancelBooking(@PathVariable Long id) {

        bookingService.cancelBooking(id);
    }
    @PostMapping("/fare/{userId}")
    public void calculateFare(@PathVariable Long userId, @RequestParam double distance,@RequestBody BookingRequest request){
        bookingService.book(userId, distance, request);
    }


}
