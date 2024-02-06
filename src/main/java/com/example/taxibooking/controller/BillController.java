package com.example.taxibooking.controller;

import com.example.taxibooking.contract.request.BillRequest;
import com.example.taxibooking.contract.response.BillResponse;
import com.example.taxibooking.contract.response.UpdateAccountResponse;
import com.example.taxibooking.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/taxi-booking")
@RequiredArgsConstructor
public class BillController {
    @Autowired private final BillService billService;

    @PostMapping("/fare")
    public @ResponseBody BillResponse calculateFare(@RequestBody BillRequest request) {
        return billService.calculateFare(request);
    }

    @GetMapping("/{id}")
    public @ResponseBody UpdateAccountResponse balanceCheck(
            @PathVariable Long id, @RequestParam Double accountBalance, @RequestParam Double fare) {
        return billService.balanceCheck(id, accountBalance, fare);
    }
}
