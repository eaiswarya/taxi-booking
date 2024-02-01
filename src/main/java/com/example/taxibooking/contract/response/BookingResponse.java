package com.example.taxibooking.contract.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data

public class BookingResponse {
    private String id;
    private String pickupLocation;
    private String dropoutLocation;
    private LocalDateTime booking_time;

}
