package com.example.taxibooking.contract.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class SignUpResponse {
    private Long id;
    private String name;
    private String email;
}
