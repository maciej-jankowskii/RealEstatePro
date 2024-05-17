package com.realestate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ApartmentPropertyDto {
    private Long id;
    @NotBlank(message = "Address cannot be empty")
    private String address;
    @NotNull(message = "Price cannot be empty")
    @Positive(message = "Price must be a positive value")
    private BigDecimal price;
    private String description;
    @NotNull(message = "Area cannot be empty")
    @Positive(message = "Area must be a positive value")
    private Double area;
    @NotNull(message = "Number of rooms cannot be empty")
    @Positive(message = "Number of rooms must be a positive value")
    private Integer rooms;
    @NotNull(message = "Number of bathrooms cannot be empty")
    @Positive(message = "Number of bathrooms must be a positive value")
    private Integer bathrooms;
    private Boolean duplexApartment;
    @NotBlank(message = "You have to choice type of building")
    private String buildingType;
    private Integer floor;
    private Boolean elevator;
    private Boolean balcony;
    private Boolean garage;
    private Integer yearOfConstruction;
    @NotBlank(message = "You have to choice standard of property")
    private String standard;

}
