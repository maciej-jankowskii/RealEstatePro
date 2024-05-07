package com.realestate.repository;

import com.realestate.model.offer.Offer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OffersRepository extends CrudRepository<Offer, Long> {

    List<Offer> findAllByClient_Id(Long id);

    Offer findByReservation_Id(Long reservationId);
    boolean existsByPropertyId(Long propertyId);

    Optional<Offer> findById(Long id);
}
