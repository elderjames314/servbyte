package com.servbyte.ecommerce.service;

import com.servbyte.ecommerce.dtos.RestaurantDto;
import com.servbyte.ecommerce.entities.Cities;
import com.servbyte.ecommerce.entities.Restaurant;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RestaurantService {
     void registerRestaurant(RestaurantDto restaurantDto);
     List<Restaurant> getAllRestaurants();
     List<Restaurant> findRestaurantsByCity(String city);
     List<Cities> fetchAllCities();
}
