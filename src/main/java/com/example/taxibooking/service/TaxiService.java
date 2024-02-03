package com.example.taxibooking.service;

import com.example.taxibooking.contract.request.TaxiRequest;
import com.example.taxibooking.contract.response.TaxiResponse;
import com.example.taxibooking.model.Taxi;
import com.example.taxibooking.repository.TaxiRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
@RequestMapping("/taxi-booking")
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

    public List<TaxiResponse> findAvailableTaxis(String pickupLocation) {
        List<Taxi> availableTaxis = taxiRepository.findByCurrentLocation(pickupLocation);
        return availableTaxis.stream()
                .map(taxi -> modelMapper.map(taxi, TaxiResponse.class))
                .collect(Collectors.toList());
    }
}
