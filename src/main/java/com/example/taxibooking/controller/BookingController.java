package com.example.taxibooking.controller;

import com.example.taxibooking.contract.request.BookingRequest;
import com.example.taxibooking.contract.response.BookingResponse;
import com.example.taxibooking.service.BookingService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/addBooking/{userId}")
    public @ResponseBody BookingResponse addBooking(
            @Valid @PathVariable Long userId,
            @RequestParam double distance,
            @RequestBody BookingRequest request) {
        return bookingService.addBooking(userId, distance, request);
    }

    @GetMapping("/details")
    public @ResponseBody List<BookingResponse> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/details/{id}")
    public BookingResponse getBooking(@PathVariable Long id) {
        return bookingService.getBooking(id);
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long id) {
        String cancelResponse = bookingService.cancelBooking(id);
        return ResponseEntity.ok(cancelResponse);
    }
}
