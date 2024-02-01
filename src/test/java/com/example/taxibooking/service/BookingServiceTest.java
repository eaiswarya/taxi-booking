package com.example.taxibooking.service;


import com.example.taxibooking.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;

import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import static org.mockito.Mockito.mock;

public class BookingServiceTest {

    private BookingRepository bookingRepository;
    private ModelMapper modelMapper;
    private BookingService bookingService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        bookingRepository = mock(BookingRepository.class);
        modelMapper = mock(ModelMapper.class);
        bookingService = new BookingService(bookingRepository, modelMapper);
    }


}
