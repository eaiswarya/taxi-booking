package com.example.taxibooking.contract.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BillResponse {
    private Long id;
    private Double fare;
}
