package com.example.taxibooking.contract.response;


import com.example.taxibooking.constant.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Builder
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
    @Enumerated(EnumType.STRING)
    private Status status;

}
