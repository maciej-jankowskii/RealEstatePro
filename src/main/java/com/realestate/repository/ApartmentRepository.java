package com.realestate.repository;


import com.realestate.model.Property.Apartment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ApartmentRepository extends CrudRepository<Apartment, Long> {

    Page<Apartment> findAll(Pageable pageable);
}
