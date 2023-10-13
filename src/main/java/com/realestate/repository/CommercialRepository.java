package com.realestate.repository;

import com.realestate.model.Property.CommercialProperty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommercialRepository extends CrudRepository<CommercialProperty, Long> {
}
