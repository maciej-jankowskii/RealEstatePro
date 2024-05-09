package com.realestate.repository;

import com.realestate.model.Property.CommercialProperty;
import com.realestate.model.Property.House;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseRepository extends CrudRepository<House, Long> {
    Page<House> findAll(Pageable pageable);
}
