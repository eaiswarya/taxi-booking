package com.example.taxibooking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.taxibooking.contract.request.LoginRequest;
import com.example.taxibooking.contract.request.SignUpRequest;
import com.example.taxibooking.contract.request.UpdateAccountRequest;
import com.example.taxibooking.contract.response.LoginResponse;
import com.example.taxibooking.contract.response.SignUpResponse;
import com.example.taxibooking.contract.response.UpdateAccountResponse;
import com.example.taxibooking.exception.EntityAlreadyExistsException;
import com.example.taxibooking.exception.EntityNotFoundException;
import com.example.taxibooking.exception.InvalidUserException;
import com.example.taxibooking.model.User;
import com.example.taxibooking.repository.UserRepository;
import com.example.taxibooking.security.JwtService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
        userRepository = Mockito.mock(UserRepository.class);
        jwtService = Mockito.mock(JwtService.class);
        modelMapper = Mockito.mock(ModelMapper.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserService(userRepository, modelMapper, passwordEncoder, jwtService);
    }

    @Test
    void testSignup() {
        SignUpRequest request = new SignUpRequest("Aswa", "aswa@gmail.com", "aswa@123");
        User user = modelMapper.map(request, User.class);
        SignUpResponse expectedResponse = modelMapper.map(user, SignUpResponse.class);

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);
        assertThrows(EntityAlreadyExistsException.class, () -> userService.signUp(request));

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("aswa@123")).thenReturn("aswa@123");

        when(userRepository.save(any())).thenReturn(user);

        SignUpResponse actualResponse = userService.signUp(request);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testLogin() {
        User user = new User(1L, "aswa", "aswa@gmail.com", "aswa@123", 0.0);
        LoginRequest request = new LoginRequest("aswa@gmail.com", "aswa@123");
        LoginResponse expectedResponse = new ModelMapper().map(request, LoginResponse.class);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(!passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);
        assertThrows(InvalidUserException.class, () -> userService.login(request));
        when(!passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);

        LoginResponse actualResponse = userService.login(request);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testAddBalance() {
        Long id = 1L;
        Double accountBalance = 100.0;
        User user = User.builder().id(id).name("name").accountBalance(accountBalance).build();
        UpdateAccountResponse expectedResponse = new UpdateAccountResponse(1L, "name", 100.0);
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
        UpdateAccountResponse expectedResponse = new UpdateAccountResponse(id, "name", 100.0);
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
        assertThrows(
                EntityNotFoundException.class, () -> userService.addBalance(id, accountBalance));
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
