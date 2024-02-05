package com.example.taxibooking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//import com.example.taxibooking.contract.request.SignUpRequest;
import com.example.taxibooking.contract.request.SignUpRequest;
import com.example.taxibooking.contract.request.UpdateAccountRequest;
//import com.example.taxibooking.contract.response.SignUpResponse;
import com.example.taxibooking.contract.response.SignUpResponse;
import com.example.taxibooking.contract.response.UpdateAccountResponse;
import com.example.taxibooking.exception.EntityAlreadyExistsException;
import com.example.taxibooking.exception.EntityNotFoundException;
import com.example.taxibooking.model.User;
import com.example.taxibooking.repository.UserRepository;
//import com.example.taxibooking.security.JwtService;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;
//    private JwtService jwtService;

   @BeforeEach
    public void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);   }
    @Test
    void testSignUp() {
    SignUpRequest request = new SignUpRequest("user", "user@example.com", "password");
    User user = User.builder()
            .name(request.getName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .accountBalance(0D)
            .build();

    SignUpResponse expectedResponse = new SignUpResponse(1L, "user", "user@example.com");
    when(userRepository.existsByEmail(any(String.class))).thenReturn(false);
    when(userRepository.save(any(User.class))).thenReturn(user);
    when(modelMapper.map(any(User.class), eq(SignUpResponse.class))).thenReturn(expectedResponse);
    SignUpResponse actualResponse = userService.signUp(request);
    assertEquals(expectedResponse, actualResponse);
    verify(userRepository, times(1)).save(any(User.class));
    verify(modelMapper, times(1)).map(any(User.class), eq(SignUpResponse.class));
    }


    @Test
    void testAddBalance() {
        Long id = 1L;
        Double accountBalance = 100.0;
        User user = User.builder().id(id).name("name").accountBalance(accountBalance).build();
        UpdateAccountResponse expectedResponse = new UpdateAccountResponse(1L, "name", "name@gmail.com", 100.0);
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
        UpdateAccountResponse expectedResponse = new UpdateAccountResponse(id, "name", "name@gmail.com", 100.0);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(user, UpdateAccountResponse.class)).thenReturn(expectedResponse);
        UpdateAccountResponse actualResponse = userService.updateBalance(id, request);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testAddBalance_UserNotFound() {
        Long id = 1L;
        Double accountBalance = 100.0;
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.addBalance(id, accountBalance));
    }

    @Test
    void testUpdateBalance_UserNotFound() {
        Long id = 1L;
        UpdateAccountRequest request = new UpdateAccountRequest(100.0);
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.updateBalance(id, request));
    }

    @Test
    void signUp_WhenEmailExists_ThrowsEntityAlreadyExistsException() {
        SignUpRequest request = new SignUpRequest("name", "name@gmail.com", "password");
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);
        assertThrows(EntityAlreadyExistsException.class, () -> userService.signUp(request));

    }

}
