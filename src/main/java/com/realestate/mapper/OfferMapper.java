package com.realestate.mapper;

import com.realestate.dto.OfferDto;
import com.realestate.exceptions.ResourceNotFoundException;
import com.realestate.model.Property.Property;
import com.realestate.model.client.Client;
import com.realestate.model.offer.Offer;
import com.realestate.model.user.UserEmployee;
import com.realestate.repository.ClientRepository;
import com.realestate.repository.PropertyRepository;
import com.realestate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OfferMapper {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;


    public OfferDto map(Offer offer) {
        OfferDto dto = new OfferDto();
        dto.setId(offer.getId());
        dto.setPropertyId(offer.getProperty().getId());
        dto.setClientId(offer.getClient().getId());
        dto.setUserId(offer.getUser().getId());
        dto.setIsBooked(offer.getIsBooked());
        dto.setIsAvailable(offer.getIsAvailable());

        return dto;
    }

    public Offer map(OfferDto dto) {
        Offer offer = new Offer();
        offer.setId(dto.getId());
        Property property = propertyRepository.findById(dto.getPropertyId()).orElseThrow(() -> new ResourceNotFoundException("Property not found"));
        offer.setProperty(property);
        Client client = clientRepository.findById(dto.getClientId()).orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        offer.setClient(client);
        UserEmployee userEmployee = userRepository.findById(dto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        offer.setUser(userEmployee);
        offer.setIsBooked(dto.getIsBooked());
        offer.setIsAvailable(dto.getIsAvailable());

        return offer;
    }

}
