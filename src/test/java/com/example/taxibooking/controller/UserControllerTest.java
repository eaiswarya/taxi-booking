package com.example.taxibooking.controller;

import com.example.taxibooking.contract.request.UpdateAccountRequest;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void testUpdateBalance() throws Exception {
        Long id = 1L;
        UpdateAccountRequest request = new UpdateAccountRequest(100);
        UpdateAccountResponse expectedResponse = new UpdateAccountResponse(1L,100);
        when(userService.updateBalance(any(Long.class), any(UpdateAccountRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(
                        put("/user/updateBalance/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));
    }

}
