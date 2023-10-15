package com.realestate.mapper;

import com.realestate.dto.ReservationDto;
import com.realestate.model.offer.Offer;
import com.realestate.model.reservation.Reservation;
import com.realestate.repository.OffersRepository;
import org.springframework.stereotype.Service;

@Service
public class ReservationMapper {

    private final OffersRepository offersRepository;

    public ReservationMapper(OffersRepository offersRepository) {
        this.offersRepository = offersRepository;
    }


    public ReservationDto map(Reservation reservation){
        ReservationDto dto = new ReservationDto();
        dto.setId(reservation.getId());
        dto.setDescription(reservation.getDescription());
        dto.setOfferId(reservation.getOffer().getId());
        return dto;
    }
    public Reservation map(ReservationDto dto){
        Reservation reservation = new Reservation();
        reservation.setId(dto.getId());
        reservation.setDescription(dto.getDescription());
        Offer offer = offersRepository.findById(dto.getOfferId()).orElseThrow();
        reservation.setOffer(offer);
        return reservation;
    }
}
