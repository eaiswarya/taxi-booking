package com.example.taxibooking.contract.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaxiResponse {
    private Long id;
    private String driverName;
    private String licenseNumber;
    private String currentLocation;
}
