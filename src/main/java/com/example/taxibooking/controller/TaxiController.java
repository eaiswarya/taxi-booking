package com.example.taxibooking.controller;

import com.example.taxibooking.contract.request.TaxiRequest;
import com.example.taxibooking.contract.response.TaxiResponse;
import com.example.taxibooking.service.TaxiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/taxi")
public class TaxiController {
    private final TaxiService taxiService;


    @PostMapping("/addingTaxi")
    public TaxiResponse addTaxi(@Valid @RequestBody TaxiRequest request){
        return taxiService.addTaxi(request);

    }
    @GetMapping("/available")
    public List<TaxiResponse> availableTaxis (@RequestParam String pickupLocation){
        return taxiService.findAvailableTaxis(pickupLocation);
    }
}
