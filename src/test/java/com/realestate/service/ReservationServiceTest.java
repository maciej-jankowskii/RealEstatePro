package com.realestate.service;

import com.realestate.model.offer.Offer;
import com.realestate.model.reservation.Reservation;
import com.realestate.repository.OffersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private ReservationMapper reservationMapper;
    @Mock
    private OffersRepository offersRepository;
    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveReservation() {
        ReservationDto dto = new ReservationDto();
        Reservation reservation = new Reservation();
        Offer offer = new Offer();
        Long offerId = 1L;
        dto.setOfferId(offerId);

        when(reservationMapper.map(dto)).thenReturn(reservation);
        when(reservationRepository.save(reservation)).thenReturn(reservation);
        when(reservationMapper.map(reservation)).thenReturn(dto);
        when(offersRepository.findById(offerId)).thenReturn(Optional.of(offer));
        when(offersRepository.save(offer)).thenReturn(offer);

        ReservationDto resultReservation = reservationService.saveReservation(dto);

        assertTrue(offer.getIsBooked());
        assertEquals(dto, resultReservation);
        assertNotNull(offer.getReservation());

    }

    @Test
    public void testGetReservationById() {
        ReservationDto dto = new ReservationDto();
        Reservation reservation = new Reservation();
        Long reservationId = 1L;

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservationMapper.map(reservation)).thenReturn(dto);

        Optional<ReservationDto> resultReservation = reservationService.getReservationById(reservationId);

        assertTrue(resultReservation.isPresent());
        assertEquals(dto, resultReservation.get());
    }

    @Test
    public void testGetReservationByOfferId() {
        Long offerId = 1L;
        Reservation reservation = new Reservation();
        ReservationDto dto = new ReservationDto();

        when(reservationRepository.getReservationByOffer_Id(offerId)).thenReturn(Optional.of(reservation));
        when(reservationMapper.map(reservation)).thenReturn(dto);

        Optional<ReservationDto> resultReservation = reservationService.getReservationByOfferId(offerId);

        assertTrue(resultReservation.isPresent());
        assertEquals(dto, resultReservation.get());

    }

    @Test
    public void testGetAllReservations() {
        List<ReservationDto> dtos = createDtoReservations();
        List<Reservation> reservations = createReservations();

        when(reservationRepository.findAll()).thenReturn(reservations);
        when(reservationMapper.map(any(Reservation.class))).thenReturn(dtos.get(0), dtos.get(1));

        List<ReservationDto> allReservation = reservationService.getAllReservation();

        assertNotNull(allReservation);
        assertEquals(dtos.size(), allReservation.size());
    }

    @Test
    public void testUpdateReservation() {
        ReservationDto dto = new ReservationDto();
        Reservation reservation = new Reservation();

        when(reservationMapper.map(dto)).thenReturn(reservation);
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        reservationService.updateReservation(dto);

        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    public void testDeleteReservation() {
        Reservation reservation = new Reservation();
        Offer offer = new Offer();
        Long reservationId = 1L;
        offer.setIsBooked(true);
        offer.setReservation(reservation);

        when(offersRepository.findByReservation_Id(reservationId)).thenReturn(offer);
        when(offersRepository.save(offer)).thenReturn(offer);

        reservationService.deleteReservation(reservationId);

        assertFalse(offer.getIsBooked());
        assertNull(offer.getReservation());
        verify(reservationRepository, times(1)).deleteById(reservationId);


    }

    private List<Reservation> createReservations() {
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation());
        reservations.add(new Reservation());
        return reservations;
    }

    private List<ReservationDto> createDtoReservations() {
        List<ReservationDto> dtos = new ArrayList<>();
        dtos.add(new ReservationDto());
        dtos.add(new ReservationDto());
        return dtos;
    }

}