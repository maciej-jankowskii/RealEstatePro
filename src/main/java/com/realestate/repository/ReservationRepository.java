package com.realestate.repository;

import com.realestate.model.reservation.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {
    Optional<Reservation> getReservationByOffer_Id(Long offerId);
    Page<Reservation> findAll(Pageable pageable);
}
