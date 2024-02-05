package com.example.taxibooking.contract.response;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class SignUpResponse {
    private Long id;
    private String name;
    private String email;
}
