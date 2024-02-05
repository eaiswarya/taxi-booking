package com.example.taxibooking.contract.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


@Getter
public class LoginRequest {
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    private String password;
}
