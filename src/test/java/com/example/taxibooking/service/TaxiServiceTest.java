package com.example.taxibooking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.taxibooking.contract.request.TaxiRequest;
import com.example.taxibooking.contract.response.TaxiResponse;
import com.example.taxibooking.model.Taxi;
import com.example.taxibooking.repository.TaxiRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

public class TaxiServiceTest {
    private TaxiRepository taxiRepository;
    private TaxiService taxiService;
    private ModelMapper modelMapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        taxiRepository = mock(TaxiRepository.class);
        modelMapper = mock(ModelMapper.class);
        taxiService = new TaxiService(taxiRepository, modelMapper);
    }

    @Test
    void testAddTaxi() {
        TaxiRequest request = new TaxiRequest("name", "ksh56", "tirur");
        Taxi taxi =
                Taxi.builder()
                        .driverName(request.getDriverName())
                        .licenseNumber(request.getLicenseNumber())
                        .currentLocation(request.getCurrentLocation())
                        .build();
        TaxiResponse expectedResponse = new TaxiResponse();
        when(taxiRepository.save(any())).thenReturn(taxi);

        when(modelMapper.map(taxi, TaxiResponse.class)).thenReturn(expectedResponse);

        TaxiResponse actualResponse = taxiService.addTaxi(request);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testFindAvailableTaxis() {
        String pickupLocation = "tirur";
        List<Taxi> availableTaxis = new ArrayList<>();
        when(taxiRepository.findByCurrentLocation(pickupLocation)).thenReturn(availableTaxis);
        List<TaxiResponse> expectedResponse = new ArrayList<>();
        List<TaxiResponse> actualResponse = taxiService.findAvailableTaxis(pickupLocation);
        assertEquals(expectedResponse, actualResponse);
        verify(taxiRepository).findByCurrentLocation(pickupLocation);
    }
}
