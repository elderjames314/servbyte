package com.servbyte.ecommerce.controllers;

import com.servbyte.ecommerce.dtos.RestaurantDto;
import com.servbyte.ecommerce.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/all-cities")
    public ResponseEntity<?> fetchAllCities(){
        return ResponseEntity.ok(restaurantService.fetchAllCities());
    }

    @PostMapping("/find/{city}")
    public ResponseEntity<?> fetchRestaurantsByCities(@RequestParam String city){
        return ResponseEntity.ok(restaurantService.findRestaurantsByCity(city));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerRestaurants(@RequestBody RestaurantDto restaurantDto){
        restaurantService.registerRestaurant(restaurantDto);
        return ResponseEntity.ok("Restaurant registered successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<?> fetchAll(){
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }
}
