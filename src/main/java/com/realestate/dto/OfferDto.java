package com.realestate.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfferDto {
    private Long id;
    @NotNull(message = "You have to choose employee ID")
    private Long userId;
    @NotNull(message = "You have to choose client ID")
    private Long clientId;
    @NotNull(message = "You have to choose property ID")
    private Long propertyId;
    private Long reservationId;
    private Boolean isBooked;
    private Boolean isAvailable;
}
