package com.realestate.repository;

import com.realestate.model.Property.CommercialProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommercialRepository extends CrudRepository<CommercialProperty, Long> {

    Page<CommercialProperty> findAll(Pageable pageable);
}
