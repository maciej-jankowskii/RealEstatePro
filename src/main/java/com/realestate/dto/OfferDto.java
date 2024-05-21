package com.realestate.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfferDto {
    private Long id;
    @NotNull(message = "You have to choose employee")
    private Long userId;
    @NotNull(message = "You have to choose client")
    private Long clientId;
    @NotNull(message = "You have to choose property")
    private Long propertyId;
    private Boolean isBooked;
    private Boolean isAvailable;
}
