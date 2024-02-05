package com.example.taxibooking.contract.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountRequest {
    @NotNull(message = "balance should not be blank")
    private Double accountBalance;
}
