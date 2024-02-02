package com.example.taxibooking.service;

import com.example.taxibooking.contract.request.SignUpRequest;
import com.example.taxibooking.contract.response.SignUpResponse;
import com.example.taxibooking.model.User;
import com.example.taxibooking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        userRepository = mock(UserRepository.class);
        modelMapper = mock(ModelMapper.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserService(userRepository, modelMapper, passwordEncoder);
    }

    @Test
    void testSignUp() throws Exception {
        SignUpRequest request = new SignUpRequest("akshay", "akshay@gmail.com", "Akshay@123");

        User user = new User(1L, "akshay", "akshay@gmail.com", "Akshy@123", 100.0);
        SignUpResponse expectedResponse = new SignUpResponse(1L, "akshay", "akshay@gmail.com");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(user, SignUpResponse.class)).thenReturn(expectedResponse);
        SignUpResponse actualResponse = userService.signUp(request);
        assertEquals(expectedResponse, actualResponse);
        verify(userRepository, times(1)).existsByEmail(request.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
        verify(modelMapper, times(1)).map(user, SignUpResponse.class);
    }


}
