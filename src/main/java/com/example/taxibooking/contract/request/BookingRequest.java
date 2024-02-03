package com.example.taxibooking.contract.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
    @NotBlank(message = "Please enter")
    private String pickupLocation;

    @NotBlank(message = "Please enter")
    private String dropoutLocation;
}
