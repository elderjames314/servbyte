package com.servbyte.ecommerce.dtos;

import com.servbyte.ecommerce.entities.Restaurant;
import lombok.Data;

import java.util.List;

@Data
public class RestaurantMenuDto {
    private String name;
    private double price;
    private int preparationTime;
    private List<String> pictures;
    private String description;
    private Restaurant restaurant;
}
