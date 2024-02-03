package com.example.taxibooking.contract.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountRequest {
    @NotNull(message = "balance should not be blank")
    private double accountBalance;
}
