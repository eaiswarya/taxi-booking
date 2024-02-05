package com.example.taxibooking.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.taxibooking.constant.Status;
import com.example.taxibooking.contract.request.BookingRequest;
import com.example.taxibooking.contract.request.SignUpRequest;
import com.example.taxibooking.contract.response.BookingResponse;
import com.example.taxibooking.contract.response.SignUpResponse;
import com.example.taxibooking.contract.response.TaxiResponse;
import com.example.taxibooking.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
@AutoConfigureMockMvc
public class BookingControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockBean private BookingService bookingService;

//    @Test
//    void testAddBooking() throws Exception {
//       Long id=1L;
//       BookingRequest request = new BookingRequest("location","location2");
//       BookingResponse expectedResponse = new BookingResponse(1L,"location1","location2",LocalDateTime.now(),100.0,Status.BOOKED);
//       when(bookingService.addBooking(request)).thenReturn(expectedResponse);
//
//
//    }

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
        String expectedResponse = "Booking cancelled successfully";

        doNothing().when(bookingService).cancelBooking(any(Long.class));

        mockMvc.perform(post("/booking/cancel/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    void testSearchTaxi() throws Exception {
        Long id = 1L;
        String pickupLocation = "Location1";
        List<TaxiResponse> expectedResponse = new ArrayList<>();

        when(bookingService.searchTaxi(any(Long.class), any(String.class)))
                .thenReturn(expectedResponse);

        mockMvc.perform(
                        get("/booking/nearestTaxi")
                                .param("userId", id.toString())
                                .param("pickupLocation", pickupLocation))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));
    }



}
