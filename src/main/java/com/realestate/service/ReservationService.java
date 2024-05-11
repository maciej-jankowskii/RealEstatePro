package com.realestate.service;

import com.realestate.dto.ReservationDto;
import com.realestate.exceptions.ResourceNotFoundException;
import com.realestate.mapper.ReservationMapper;
import com.realestate.model.offer.Offer;
import com.realestate.model.reservation.Reservation;
import com.realestate.repository.OffersRepository;
import com.realestate.repository.ReservationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        Offer offer = offersRepository.findById(dto.getOfferId()).orElseThrow(() -> new ResourceNotFoundException("Offer not found"));
        offer.setIsBooked(true);
        offer.setReservation(saved);
        offersRepository.save(offer);
        return reservationMapper.map(saved);
    }

    public ReservationDto getReservationById(Long id){
        return reservationRepository.findById(id).map(reservationMapper::map).orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
    }

    public ReservationDto getReservationByOfferId(Long offerId){
        return reservationRepository.getReservationByOffer_Id(offerId).map(reservationMapper::map).orElseThrow(() -> new ResourceNotFoundException("Offer not found"));
    }

    public List<ReservationDto> getAllReservation(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Reservation> reservationPage = reservationRepository.findAll(pageable);
        List<ReservationDto> dtos = reservationPage.getContent().stream().map(reservationMapper::map).collect(Collectors.toList());
        return dtos;
    }
    @Transactional
    public void updateReservation(Long id, ReservationDto updateDto){
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
        updateReservation(updateDto, reservation);
        reservationRepository.save(reservation);
    }

    public void deleteReservation(Long id){
        Offer offer = offersRepository.findByReservation_Id(id).orElseThrow(() -> new ResourceNotFoundException("Offer not foound"));
        offer.setIsBooked(false);
        offer.setReservation(null);
        offersRepository.save(offer);
        reservationRepository.deleteById(id);
    }

    private void updateReservation(ReservationDto updateDto, Reservation reservation) {
        reservation.setDescription(updateDto.getDescription());
        Offer offer = offersRepository.findById(updateDto.getOfferId()).orElseThrow(() -> new ResourceNotFoundException("Offer not found"));
        reservation.setOffer(offer);
    }

}
