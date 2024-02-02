package com.example.taxibooking.controller;


import com.example.taxibooking.constant.Status;
import com.example.taxibooking.contract.request.BookingRequest;
import com.example.taxibooking.contract.response.BookingResponse;
import com.example.taxibooking.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookingService bookingService;

    @Test
    void testAddBooking() throws Exception{
        BookingRequest request=new BookingRequest("Location1","Location2");
        BookingResponse expectedResponse=new BookingResponse(1L,"location1","location2",LocalDateTime.now(),100.0,Status.BOOKED);

        when(bookingService.addBooking(any(BookingRequest.class))).thenReturn(expectedResponse);
        mockMvc.perform(
                post("/booking/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));
    }
    @Test
    void testGetAllBookings() throws Exception {
        List<BookingResponse> responses = Arrays.asList(new BookingResponse());

        when(bookingService.getAllBookings()).thenReturn(responses);

        mockMvc.perform(get("/details").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(responses)));
    }
    @Test
    void testGetBookingById() throws Exception {
        Long id = 1L;
        BookingResponse expectedResponse = new BookingResponse();

        when(bookingService.getBooking(id)).thenReturn(expectedResponse);

        mockMvc.perform(get("/details/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));
    }
    @Test
    void testCancelBooking() throws Exception {
        Long id = 1L;

        doNothing().when(bookingService).cancelBooking(id);

        mockMvc.perform(
                        delete("/booking/" + "/cancel/" + id)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(bookingService).cancelBooking(id);
    }




}
