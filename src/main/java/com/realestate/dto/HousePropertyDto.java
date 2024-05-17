package com.realestate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class HousePropertyDto {
    private Long id;
    @NotBlank(message = "Address cannot be empty")
    private String address;
    @NotNull(message = "Price cannot be empty")
    @Positive(message = "Price must be a positive value")
    private BigDecimal price;
    private String description;
    @NotNull(message = "Land area cannot be empty")
    @Positive(message = "Land area must be a positive value")
    private Double landArea;
    @NotNull(message = "House area cannot be empty")
    @Positive(message = "House area must be a positive value")
    private Double houseArea;
    @NotNull(message = "Number of rooms cannot be empty")
    @Positive(message = "Number of rooms must be a positive value")
    private Integer rooms;
    @NotNull(message = "Number of bathrooms cannot be empty")
    @Positive(message = "Number of bathrooms must be a positive value")
    private Integer bathrooms;
    private Boolean balcony;
    private Boolean garage;
    private Boolean twoStoryHouse;
    @NotBlank(message = "You have to choice type of building")
    private String buildingType;
    private Integer yearOfConstruction;
    @NotBlank(message = "You have to choice standard of house")
    private String standard;
}
