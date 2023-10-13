package com.realestate.dto;

import com.realestate.enums.TypeOfLand;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LandPropertyDto {
    private Long id;
    private String address;
    private BigDecimal price;
    private String description;
    private Boolean available;
    private Boolean booked;
    private String typeOfLand;
    private Double area;
    private Boolean buildingPermit;
}
