package com.example.taxibooking.controller;

import com.example.taxibooking.contract.request.BillRequest;

import com.example.taxibooking.contract.response.BillResponse;
import com.example.taxibooking.contract.response.UpdateAccountResponse;
import com.example.taxibooking.service.BillService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BillControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BillService billService;

    @Test
    void testCalculateBill() throws Exception {
        BillRequest request = new BillRequest(3.0);
        BillResponse expectedResponse =
                new BillResponse(1L, 100.0);
        when(billService.calculateFare(request)).thenReturn(expectedResponse);

        mockMvc.perform(
                        post("/taxi-booking/fare")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));

    }
//    @Test
//    void testBalanceCheck() throws Exception {
//        Long id = 1L;
//        Double accountBalance = 100.0;
//        Double fare = 50.0;
//        UpdateAccountResponse expectedResponse = new UpdateAccountResponse(1L,"name","name@gmail.com",100.0);
//
//        when(billService.balanceCheck(id, accountBalance, fare)).thenReturn(expectedResponse);
//        mockMvc.perform(get("/{id}", id)
//                        .param("accountBalance", accountBalance.toString())
//                        .param("fare", fare.toString())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));
//    }


}
