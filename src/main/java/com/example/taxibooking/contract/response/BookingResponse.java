package com.example.taxibooking.contract.response;

import com.example.taxibooking.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BookingResponse {
    private Long id;
    private String pickupLocation;
    private String dropoutLocation;
    private Double distance;
    private Double fare;
    private Status status;
}
