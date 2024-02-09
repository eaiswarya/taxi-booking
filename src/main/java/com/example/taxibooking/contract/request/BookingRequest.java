package com.example.taxibooking.contract.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookingRequest {
    private String pickupLocation;

    private String dropoutLocation;
}
