package com.example.taxibooking.service;

import com.example.taxibooking.contract.request.TaxiRequest;
import com.example.taxibooking.contract.response.TaxiResponse;
import com.example.taxibooking.model.Taxi;
import com.example.taxibooking.repository.TaxiRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaxiService {
    private final TaxiRepository taxiRepository;
    private final ModelMapper modelMapper;

    public TaxiResponse addTaxi(TaxiRequest request) {
        Taxi taxi =
                Taxi.builder()
                        .driverName(request.getDriverName())
                        .licenseNumber(request.getLicenseNumber())
                        .currentLocation(request.getCurrentLocation())
                        .build();
        taxi = taxiRepository.save(taxi);
        return modelMapper.map(taxi, TaxiResponse.class);
    }

    public List<Taxi> findAvailableTaxis(String pickupLocation) {
        List<Taxi> allTaxis = taxiRepository.findAll();
        List<Taxi> availableTaxis = new ArrayList<>();
        for (Taxi taxis : allTaxis) {
            if (taxis.getCurrentLocation().equals(pickupLocation)) {
                availableTaxis.add(taxis);
            }
        }
        if (availableTaxis.isEmpty()) {
            throw new EntityNotFoundException();
        } else {
            return availableTaxis.stream()
                    .map(taxi -> modelMapper.map(taxi, Taxi.class))
                    .collect(Collectors.toList());
        }
    }
}
