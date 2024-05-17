package com.realestate.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationDto {

    private Long id;
    private String description;
    @NotNull(message = "You have to choose offer ID")
    private Long offerId;
}
