package com.realestate.repository;

import com.realestate.model.offer.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OffersRepository extends CrudRepository<Offer, Long> {

    List<Offer> findAllByClient_Id(Long id);

    boolean existsByPropertyId(Long propertyId);

    Optional<Offer> findById(Long id);

    Page<Offer> findAll(Pageable pageable);
}
