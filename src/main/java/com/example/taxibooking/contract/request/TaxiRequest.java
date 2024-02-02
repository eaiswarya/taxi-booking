package com.example.taxibooking.contract.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaxiRequest {
    private String driverName;
    private String licenseNumber;
    private String currentLocation;
}
