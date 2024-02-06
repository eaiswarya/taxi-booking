package com.example.taxibooking.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.taxibooking.contract.request.LoginRequest;
import com.example.taxibooking.contract.request.SignUpRequest;
import com.example.taxibooking.contract.request.UpdateAccountRequest;
import com.example.taxibooking.contract.response.LoginResponse;
import com.example.taxibooking.contract.response.SignUpResponse;
import com.example.taxibooking.contract.response.UpdateAccountResponse;
import com.example.taxibooking.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
    @Autowired private MockMvc mockMvc;

    @MockBean private UserService userService;

    @Test
    void testUserSignup() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest("user", "user@example.com", "password");
        SignUpResponse expectedResponse = new SignUpResponse(1L, "user", "user@example.com");
        when(userService.signUp(any(SignUpRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/user/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(signUpRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));
    }

    @Test
    void testLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest("aswa@gmail.com", "aswa@123");
        LoginResponse expectedResponse = new LoginResponse("testToken");
        when(userService.login(any(LoginRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/user/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));
    }

    @Test
    void testAddBalance() throws Exception {
        Long id = 1L;
        Double accountBalance = 100.0;
        UpdateAccountResponse expectedResponse =
                new UpdateAccountResponse(1L, "name", 100.0);
        when(userService.addBalance(any(Long.class), any(Double.class)))
                .thenReturn(expectedResponse);
        mockMvc.perform(
                        put("/user/" + id + "/addBalance")
                                .param("accountBalance", accountBalance.toString())
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));
    }

    @Test
    void testUpdateBalance() throws Exception {
        Long id = 1L;
        UpdateAccountRequest request = new UpdateAccountRequest(100.0);
        UpdateAccountResponse expectedResponse =
                new UpdateAccountResponse(1L, "name", 100.0);
        when(userService.updateBalance(any(Long.class), any(UpdateAccountRequest.class)))
                .thenReturn(expectedResponse);

        mockMvc.perform(
                        put("/user/updateBalance/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));
    }
}
