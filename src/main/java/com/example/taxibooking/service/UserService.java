package com.example.taxibooking.service;


import com.example.taxibooking.contract.request.LoginRequest;
import com.example.taxibooking.contract.request.SignupRequest;
import com.example.taxibooking.contract.response.SignupResponse;


import com.example.taxibooking.exception.InvalidLoginException;
import com.example.taxibooking.model.User;
import com.example.taxibooking.repository.UserRepository;
import jakarta.persistence.EntityExistsException;


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


    public SignupResponse signUp(SignupRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {

            throw new EntityExistsException("Invalid Signup");
        }

        User user =
                User.builder()
                        .name(request.getName())
                        .email(request.getEmail())
                        .password(request.getPassword())
                        .build();

        user = userRepository.save(user);

        return modelMapper.map(user, SignupResponse.class);
    }

    public Long login(LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        if (!userRepository.existsByEmail(email)) {

            throw new EntityNotFoundException("Invalid login");
        }

        User user = (User) userRepository.findByEmail(request.getEmail()).orElseThrow();

        if (passwordEncoder.matches(password, user.getPassword())) {

            return user.getId();
        }

        throw new InvalidLoginException();
    }

}





