package com.example.taxibooking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.taxibooking.contract.request.SignUpRequest;
import com.example.taxibooking.contract.request.UpdateAccountRequest;
import com.example.taxibooking.contract.response.SignUpResponse;
import com.example.taxibooking.contract.response.UpdateAccountResponse;
import com.example.taxibooking.model.User;
import com.example.taxibooking.repository.UserRepository;
import com.example.taxibooking.security.JwtService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceTest {
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private JwtService jwtService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        userRepository = mock(UserRepository.class);
        modelMapper = mock(ModelMapper.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtService = mock(JwtService.class);
        userService = new UserService(userRepository, modelMapper, passwordEncoder, jwtService);
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

    @Test
    void testAddBalance() {
        Long id = 1L;
        Double accountBalance = 100.0;
        User user = User.builder().id(id).name("name").accountBalance(accountBalance).build();
        UpdateAccountResponse expectedResponse = new UpdateAccountResponse(1L, 100.0);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(user, UpdateAccountResponse.class)).thenReturn(expectedResponse);
        UpdateAccountResponse actualResponse = userService.addBalance(id, accountBalance);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testUpdateBalance() {
        Long id = 1L;
        UpdateAccountRequest request = new UpdateAccountRequest(100.0);
        User user =
                User.builder()
                        .id(id)
                        .name("user")
                        .email("user@example.com")
                        .accountBalance(200.0)
                        .build();
        UpdateAccountResponse expectedResponse = new UpdateAccountResponse(id, 100.0);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(user, UpdateAccountResponse.class)).thenReturn(expectedResponse);
        UpdateAccountResponse actualResponse = userService.updateBalance(id, request);
        assertEquals(expectedResponse, actualResponse);
    }
}
