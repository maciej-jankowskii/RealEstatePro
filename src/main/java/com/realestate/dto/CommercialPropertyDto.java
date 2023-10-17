package com.realestate.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CommercialPropertyDto {
    private Long id;
    private String address;
    private BigDecimal price;
    private String description;
    private Double area;
    private Integer rooms;
    private Integer bathrooms;
    private String buildingType;
    private Integer floor;
    private String typeOfBusiness;
}
