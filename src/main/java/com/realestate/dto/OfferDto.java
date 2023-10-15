package com.realestate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfferDto {
    private Long id;
    private Long userId;
    private Long clientId;
    private Long propertyId;
    private Long reservationId;
    private Boolean isBooked;
    private Boolean isAvailable;
}
