package com.example.taxibooking.controller;


import com.example.taxibooking.contract.request.LoginRequest;
import com.example.taxibooking.contract.request.SignupRequest;
import com.example.taxibooking.contract.response.SignupResponse;
import com.example.taxibooking.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signUp(@Valid @RequestBody SignupRequest request) {
        return ResponseEntity.ok(userService.signUp(request));
    }

    @PostMapping("/login")
    public Long login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request);
    }

}
