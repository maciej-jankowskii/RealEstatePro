package com.realestate.mapper;

import com.realestate.dto.OfferAvailableDto;
import com.realestate.model.offer.Offer;
import org.springframework.stereotype.Service;

@Service
public class OfferAvailableMapper {

    public OfferAvailableDto map(Offer offer){
        OfferAvailableDto dto = new OfferAvailableDto();
        dto.setId(offer.getId());
        dto.setPropertyAddress(offer.getProperty().getAddress());
        dto.setPropertyPrice(String.valueOf(offer.getProperty().getPrice()));
        dto.setPropertyDescription(offer.getProperty().getDescription());
        dto.setAgentEmailToContact(offer.getUser().getEmail());
        dto.setIsBooked(offer.getIsBooked());
        dto.setIsAvailable(true);
        return dto;
    }
}
