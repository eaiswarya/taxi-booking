package com.example.taxibooking.contract.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;


@Data
@Getter
@AllArgsConstructor
public class UpdateAccountResponse {
    private Long id;
    private String name;
    private String email;
    private Double accountBalance;
}
