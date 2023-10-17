package com.realestate.repository;

import com.realestate.dto.ApartmentPropertyDto;
import com.realestate.model.Property.Apartment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ApartmentRepository extends CrudRepository<Apartment, Long> {


}
