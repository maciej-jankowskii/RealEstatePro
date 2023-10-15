package com.realestate.mapper;

import com.realestate.dto.OfferDto;
import com.realestate.model.Property.Property;
import com.realestate.model.client.Client;
import com.realestate.model.offer.Offer;
import com.realestate.model.reservation.Reservation;
import com.realestate.model.user.UserEmployee;
import com.realestate.repository.ClientRepository;
import com.realestate.repository.PropertyRepository;
import com.realestate.repository.ReservationRepository;
import com.realestate.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class OfferMapper {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ReservationRepository reservationRepository;

    public OfferMapper(PropertyRepository propertyRepository, UserRepository userRepository, ClientRepository clientRepository, ReservationRepository reservationRepository) {
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.reservationRepository = reservationRepository;
    }

    public OfferDto map(Offer offer){
        OfferDto dto = new OfferDto();
        dto.setId(offer.getId());
        dto.setPropertyId(offer.getProperty().getId());
        dto.setClientId(offer.getClient().getId());
        dto.setUserId(offer.getUser().getId());
        dto.setIsBooked(offer.getIsBooked());
        dto.setIsAvailable(offer.getIsAvailable());
        return dto;
    }

    public Offer map(OfferDto dto){
        Offer offer = new Offer();
        offer.setId(dto.getId());
        Property property = propertyRepository.findById(dto.getPropertyId()).orElseThrow();
        offer.setProperty(property);
        Client client = clientRepository.findById(dto.getClientId()).orElseThrow();
        offer.setClient(client);
        UserEmployee userEmployee = userRepository.findById(dto.getUserId()).orElseThrow();
        offer.setUser(userEmployee);
        offer.setIsBooked(dto.getIsBooked());
        offer.setIsAvailable(dto.getIsAvailable());
        return offer;
    }

}
