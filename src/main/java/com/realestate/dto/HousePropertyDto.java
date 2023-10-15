package com.realestate.dto;

import com.realestate.enums.BuildingType;
import com.realestate.enums.Standard;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class HousePropertyDto {
    private Long id;
    private String address;
    private BigDecimal price;
    private String description;
    private Double landArea;
    private Double houseArea;
    private Integer rooms;
    private Integer bathrooms;
    private Boolean balcony;
    private Boolean garage;
    private Boolean twoStoryHouse;
    private String buildingType;
    private Integer yearOfConstruction;
    private String standard;
}
