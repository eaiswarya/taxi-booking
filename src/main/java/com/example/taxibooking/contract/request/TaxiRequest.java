package com.example.taxibooking.contract.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class TaxiRequest {
    @NotBlank(message = "Name should not be blank")
    private String driverName;

    private String licenseNumber;
    private String currentLocation;
}
