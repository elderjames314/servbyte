package com.servbyte.ecommerce.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LogisticsDto {
    @ApiModelProperty(required = true)
    @NotBlank
    private String companyName;
    @ApiModelProperty(required = true)
    @NotBlank
    private String logo;
    @ApiModelProperty(required = true)
    @NotBlank
    private String companyEmail;
    @ApiModelProperty(required = true)
    @NotBlank
    private String companyPhoneNumber;
    @NotBlank
    @ApiModelProperty(required = true)
    private String companyCity;
}
