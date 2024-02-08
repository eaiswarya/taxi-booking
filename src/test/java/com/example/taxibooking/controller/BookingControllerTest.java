package com.example.taxibooking.controller;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.taxibooking.contract.request.BookingRequest;
import com.example.taxibooking.contract.response.BookingResponse;
import com.example.taxibooking.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class BookingControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private BookingController bookingController;
    @MockBean private BookingService bookingService;

    @Test
    void testAddBooking() throws Exception {
        when(bookingService.addBooking(
                        Mockito.<Long>any(), anyDouble(), Mockito.<BookingRequest>any()))
                .thenReturn(new BookingResponse());
        MockHttpServletRequestBuilder postResult =
                MockMvcRequestBuilders.post("/booking/addBooking/{userId}", 1L);
        MockHttpServletRequestBuilder contentTypeResult =
                postResult
                        .param("distance", String.valueOf(10.0d))
                        .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder =
                contentTypeResult.content(
                        objectMapper.writeValueAsString(
                                new BookingRequest("Pickup Location", "Dropout Location")));
        MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content()
                                .string(
                                        "{\"id\":null,\"pickupLocation\":null,\"dropoutLocation\":null,\"fare\":null,\"status\":null}"));
    }

    @Test
    void testGetAllBookings() throws Exception {
        List<BookingResponse> responses = Arrays.asList(new BookingResponse());

        when(bookingService.getAllBookings()).thenReturn(responses);

        mockMvc.perform(get("/booking/details").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(responses)));
    }

    @Test
    void testGetBooking() throws Exception {
        long id = 1L;
        BookingResponse expectedResponse = new BookingResponse();

        when(bookingService.getBooking(id)).thenReturn(expectedResponse);

        mockMvc.perform(get("/booking/details/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));
    }

    @Test
    void testCancelBooking() throws Exception {
        Long id = 1L;
        String cancelResponse = "Booking cancelled successfully";

        when(bookingService.cancelBooking(anyLong())).thenReturn(cancelResponse);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/booking/cancel/" + id)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(cancelResponse));
    }
}
