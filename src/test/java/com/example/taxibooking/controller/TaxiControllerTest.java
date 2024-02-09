package com.example.taxibooking.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.taxibooking.contract.request.TaxiRequest;
import com.example.taxibooking.contract.response.TaxiResponse;
import com.example.taxibooking.model.Taxi;
import com.example.taxibooking.service.TaxiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TaxiControllerTest {
    @Autowired private MockMvc mockMvc;

    @MockBean private TaxiService taxiService;

    @Test
    void testAddTaxi() throws Exception {
        TaxiRequest request = new TaxiRequest("name", "dfcd67", "location1");
        TaxiResponse expectedResponse = new TaxiResponse(1L, "name", "fghj678", "location");
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
    public void testFindAvailableTaxis() throws Exception {

        String pickupLocation = "Location1";
        List<Taxi> expectedResponse =
                Arrays.asList(
                        new Taxi(1L, "name", "adhhjad7888", "location1"),
                        new Taxi(2L, "name2", "dbed7788", "location1"));

        when(taxiService.findAvailableTaxis(pickupLocation)).thenReturn(expectedResponse);

        mockMvc.perform(
                        get("/taxi/available")
                                .param("pickupLocation", pickupLocation)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));
    }
}
