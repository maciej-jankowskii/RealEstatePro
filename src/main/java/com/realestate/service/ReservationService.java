package com.realestate.service;

import com.realestate.dto.ReservationDto;
import com.realestate.mapper.ReservationMapper;
import com.realestate.model.offer.Offer;
import com.realestate.model.reservation.Reservation;
import com.realestate.repository.OffersRepository;
import com.realestate.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final OffersRepository offersRepository;

    public ReservationService(ReservationRepository reservationRepository, ReservationMapper reservationMapper, OffersRepository offersRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
        this.offersRepository = offersRepository;
    }

    @Transactional
    public ReservationDto saveReservation(ReservationDto dto){
        Reservation reservation = reservationMapper.map(dto);
        Reservation saved = reservationRepository.save(reservation);
        Offer offer = offersRepository.findById(dto.getOfferId()).orElseThrow();
        offer.setIsBooked(true);
        offer.setReservation(saved);
        offersRepository.save(offer);
        return reservationMapper.map(saved);
    }

    public Optional<ReservationDto> getReservationById(Long id){
        return reservationRepository.findById(id).map(reservationMapper::map);
    }

    public Optional<ReservationDto> getReservationByOfferId(Long offerId){
        return reservationRepository.getReservationByOffer_Id(offerId).map(reservationMapper::map);
    }

    public List<ReservationDto> getAllReservation(){
        List<Reservation> reservations = (List<Reservation>) reservationRepository.findAll();
        List<ReservationDto> dtos = reservations.stream().map(reservationMapper::map).collect(Collectors.toList());
        return dtos;
    }
    @Transactional
    public void updateReservation(ReservationDto dto){
        Reservation reservation = reservationMapper.map(dto);
        reservationRepository.save(reservation);
    }

    public void deleteReservation(Long id){
        reservationRepository.deleteById(id);
    }




}
