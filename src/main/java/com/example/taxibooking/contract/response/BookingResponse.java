package com.example.taxibooking.contract.response;

import com.example.taxibooking.constant.Status;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
public class BookingResponse {
    private Long id;
    private String pickupLocation;
    private String dropoutLocation;
    private LocalDateTime bookingTime;
    private Double fare;
    private Status status;
}
