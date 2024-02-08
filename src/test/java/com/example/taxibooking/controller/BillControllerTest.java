package com.example.taxibooking.controller;

import com.example.taxibooking.contract.response.UpdateAccountResponse;
import com.example.taxibooking.service.BillService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class BillControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockBean private BillService billService;
    @Test
    public void testBalanceAmount() throws Exception {
        Long id = 1L;
        Double accountBalance = 100.0;
        Double fare = 50.0;
        UpdateAccountResponse expectedResponse = new UpdateAccountResponse();

        when(billService.balanceCheck(eq(id), eq(accountBalance), eq(fare)))
                .thenReturn(expectedResponse);

        mockMvc.perform(
                        get("/taxi-booking/" + id)
                                .param("accountBalance", accountBalance.toString())
                                .param("fare", fare.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));
    }

}
