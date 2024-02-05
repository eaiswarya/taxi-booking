package com.example.taxibooking.contract.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class TaxiRequest {
    private String driverName;
    private String licenseNumber;
    private String currentLocation;
}
