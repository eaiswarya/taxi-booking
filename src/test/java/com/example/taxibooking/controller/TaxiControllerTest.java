package com.example.taxibooking.controller;

import com.example.taxibooking.contract.request.TaxiRequest;
import com.example.taxibooking.contract.response.TaxiResponse;
import com.example.taxibooking.service.TaxiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class TaxiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaxiService taxiService;

    @Test
    void testAddTaxi() throws Exception {
        TaxiRequest request = new TaxiRequest("name","dfcd67","location1");
        TaxiResponse expectedResponse = new TaxiResponse(1L,"name","fghj678","location");
        when(taxiService.addTaxi(any(TaxiRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(
                        post("/taxi/addingTaxi")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));
    }
    @Test
    void testAvailableTaxis() throws Exception {
        String pickupLocation = "location1";
        List<TaxiResponse> expectedResponse = new ArrayList<>();

        when(taxiService.findAvailableTaxis(any(String.class))).thenReturn(expectedResponse);

        mockMvc.perform(
                        get("/taxi/available")
                                .param("pickupLocation", pickupLocation)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));
    }




}
