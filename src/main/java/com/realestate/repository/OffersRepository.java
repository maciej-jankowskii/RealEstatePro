package com.realestate.repository;

import com.realestate.model.offer.Offer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OffersRepository extends CrudRepository<Offer, Long> {

    List<Offer> findAllByClient_Id(Long id);
}
