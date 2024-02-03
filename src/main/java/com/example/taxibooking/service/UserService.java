package com.example.taxibooking.service;

import com.example.taxibooking.contract.request.LoginRequest;
import com.example.taxibooking.contract.request.SignUpRequest;
import com.example.taxibooking.contract.request.UpdateAccountRequest;
import com.example.taxibooking.contract.response.LoginResponse;
import com.example.taxibooking.contract.response.SignUpResponse;
import com.example.taxibooking.contract.response.UpdateAccountResponse;
import com.example.taxibooking.model.User;
import com.example.taxibooking.repository.UserRepository;
import com.example.taxibooking.security.JwtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public SignUpResponse signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Invalid signup");
        }
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountBalance(0D)
                .build();
        userRepository.save(user);
        return modelMapper.map(user,SignUpResponse.class);
    }
    public LoginResponse login(LoginRequest request) throws Exception {
        String email = request.getEmail();
        String password = request.getPassword();
        if (!userRepository.existsByEmail(email)) {
            throw new EntityNotFoundException("Invalid login");
        }
        User user =userRepository.findByEmail(request.getEmail());
        if (passwordEncoder.matches(password, user.getPassword())) {
            return jwtService.generateToken(user);
        }
        throw new RuntimeException();
    }
    public UpdateAccountResponse updateBalance(Long id,UpdateAccountRequest request){
        User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        user = User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .accountBalance(user.getAccountBalance() + request.getAccountBalance())
                .build();
        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UpdateAccountResponse.class);

    }
}
