package com.realestate.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationDto {

    private Long id;
    private String description;
    private Long offerId;
}
