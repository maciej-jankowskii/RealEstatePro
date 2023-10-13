package com.realestate.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PropertyDto {
    private Long id;
    private String address;
    private BigDecimal price;
    private String description;
    private Boolean available;
    private Boolean booked;
}
