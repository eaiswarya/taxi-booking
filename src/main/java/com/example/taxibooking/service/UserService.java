package com.example.taxibooking.service;

//import com.example.taxibooking.contract.request.SignUpRequest;
import com.example.taxibooking.contract.request.SignUpRequest;
import com.example.taxibooking.contract.request.UpdateAccountRequest;

//import com.example.taxibooking.contract.response.SignUpResponse;
import com.example.taxibooking.contract.response.SignUpResponse;
import com.example.taxibooking.contract.response.UpdateAccountResponse;
//import com.example.taxibooking.exception.EntityAlreadyExistsException;

import com.example.taxibooking.exception.EntityAlreadyExistsException;
import com.example.taxibooking.exception.EntityNotFoundException;
import com.example.taxibooking.model.User;
import com.example.taxibooking.repository.UserRepository;


import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
//    private final JwtService jwtService;
@Transactional
    public SignUpResponse signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EntityAlreadyExistsException(request.getEmail());
        }
        User user =
                User.builder()
                        .name(request.getName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .accountBalance(0D)
                        .build();
        userRepository.save(user);
        return modelMapper.map(user, SignUpResponse.class);
    }

//    public LoginResponse login(LoginRequest request) {
//        User user =
//                userRepository
//                        .findByEmail(request.getEmail()).orElseThrow(InvalidLoginException::new);
//
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new InvalidLoginException();
//        }
//        String jwtToken = jwtService.generateToken(user);
//        return LoginResponse.builder().token(jwtToken).build();
//    }

    public UpdateAccountResponse addBalance(Long id, Double accountBalance) {
        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("User with id "+id+ " not found"));
        user =
                User.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .accountBalance(accountBalance)
                        .build();
        user = userRepository.save(user);
        return modelMapper.map(user, UpdateAccountResponse.class);
    }

    public UpdateAccountResponse updateBalance(Long id, UpdateAccountRequest request) {
        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("User with id "+id+ " not found"));
        user =
                User.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .accountBalance(user.getAccountBalance() + request.getAccountBalance())
                        .build();
        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UpdateAccountResponse.class);
    }
}
