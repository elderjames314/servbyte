package com.servbyte.ecommerce.controllers;

import com.servbyte.ecommerce.dtos.LogisticsDto;
import com.servbyte.ecommerce.service.LogisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/logistics")
public class LogisticsController {
    private final LogisticsService logisticsService;

    public LogisticsController(LogisticsService logisticsService) {
        this.logisticsService = logisticsService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerDeliveryCompany(@RequestBody LogisticsDto logisticsDto){
        logisticsService.registerLogisticsCompany(logisticsDto);
        return ResponseEntity.ok("Company saved successfully");


    }

    @PostMapping("/all")
    public ResponseEntity<?> getAllLogistica(){
        return ResponseEntity.ok(logisticsService.fetchAllLogisticsCompany());

    }
}
