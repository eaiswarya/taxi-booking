package com.example.taxibooking.contract.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountResponse {
    private Long id;
    private String name;
    private Double accountBalance;
}
