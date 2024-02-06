package com.example.taxibooking.contract.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookingRequest {
    @NotBlank(message = "Please enter")
    private String pickupLocation;

    @NotBlank(message = "Please enter")
    private String dropoutLocation;
}
