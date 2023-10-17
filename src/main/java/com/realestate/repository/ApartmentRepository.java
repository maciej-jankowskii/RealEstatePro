package com.realestate.repository;


import com.realestate.model.Property.Apartment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ApartmentRepository extends CrudRepository<Apartment, Long> {
}
