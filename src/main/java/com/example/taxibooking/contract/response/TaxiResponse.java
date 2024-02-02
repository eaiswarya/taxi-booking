package com.example.taxibooking.contract.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
public class TaxiResponse {
    private Long id;
    private String driverName;
    private String licenseNumber;
    private String currentLocation;
}
