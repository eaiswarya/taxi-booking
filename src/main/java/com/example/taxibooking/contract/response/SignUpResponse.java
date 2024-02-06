package com.example.taxibooking.contract.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class SignUpResponse {
    private Long id;
    private String name;
    private String email;
}
