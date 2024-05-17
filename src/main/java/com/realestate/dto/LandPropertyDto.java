package com.realestate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LandPropertyDto {
    private Long id;
    @NotBlank(message = "Address cannot be empty")
    private String address;
    @NotNull(message = "Price cannot be empty")
    @Positive(message = "Price must be a positive value")
    private BigDecimal price;
    private String description;
    @NotBlank(message = "You have to choice type of land")
    private String typeOfLand;
    @NotNull(message = "Area cannot be empty")
    @Positive(message = "Area must be a positive value")
    private Double area;
    private Boolean buildingPermit;
}
