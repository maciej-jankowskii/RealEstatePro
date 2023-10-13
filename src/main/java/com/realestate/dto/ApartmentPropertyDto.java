package com.realestate.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ApartmentPropertyDto {
    private Long id;
    private String address;
    private BigDecimal price;
    private String description;
    private Boolean available;
    private Boolean booked;
    private Double area;
    private Integer rooms;
    private Integer bathrooms;
    private Boolean duplexApartment;
    private String buildingType;
    private Integer floor;
    private Boolean elevator;
    private Boolean balcony;
    private Boolean garage;
    private Integer yearOfConstruction;
    private String standard;
}
