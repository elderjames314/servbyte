package com.servbyte.ecommerce.dtos;

import com.servbyte.ecommerce.entities.RestaurantMenu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class RestaurantDto {
    @ApiModelProperty(required = true)
    @NotBlank
    private String restaurantName;
    @NotBlank
    @ApiModelProperty(required = true)
    private String restaurantEmail;
    @NotBlank
    @ApiModelProperty(required = true)
    private String restaurantLogo;
    @ApiModelProperty(required = true)
    @NotBlank
    private List<String> listOfCities;
    @NotBlank
    @ApiModelProperty(required = true)
    private String phoneNumber;
    @ApiModelProperty(required = true)
    private List<RestaurantMenuDto> restaurantMenu;
}
