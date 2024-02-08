package com.example.taxibooking.controller;

import com.example.taxibooking.contract.request.LoginRequest;
import com.example.taxibooking.contract.request.SignUpRequest;
import com.example.taxibooking.contract.request.UpdateAccountRequest;
import com.example.taxibooking.contract.response.LoginResponse;
import com.example.taxibooking.contract.response.SignUpResponse;
import com.example.taxibooking.contract.response.UpdateAccountResponse;
import com.example.taxibooking.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public SignUpResponse userSignup(@Valid @RequestBody SignUpRequest request) {
        return userService.signUp(request);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @PutMapping("updateBalance/{id}")
    public ResponseEntity<UpdateAccountResponse> updateBalance(
            @Valid @PathVariable Long id, @RequestBody UpdateAccountRequest request) {
        return ResponseEntity.ok(userService.updateBalance(id, request));
    }
}
