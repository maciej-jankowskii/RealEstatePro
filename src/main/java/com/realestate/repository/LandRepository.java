package com.realestate.repository;

import com.realestate.model.Property.Land;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LandRepository extends CrudRepository<Land, Long> {
}
