package com.realestate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfferAvailableDto {
    private Long id;
    private String agentEmailToContact;
    private String propertyAddress;
    private String propertyPrice;
    private String propertyDescription;
    private Boolean isBooked;
    private Boolean isAvailable;
}
