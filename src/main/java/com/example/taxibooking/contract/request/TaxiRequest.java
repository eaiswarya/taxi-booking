package com.example.taxibooking.contract.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaxiRequest {
    private String driverName;
    private String licenseNumber;
    private String currentLocation;
}
