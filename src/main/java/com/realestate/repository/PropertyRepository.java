package com.realestate.repository;

import com.realestate.model.Property.Property;
import com.realestate.model.client.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends CrudRepository<Property, Long> {

    Page<Property> findAll(Pageable pageable);
    @Query("SELECT p FROM Property p WHERE CAST(p.id AS string) LIKE %:keyword% OR p.address LIKE %:keyword% OR p.description LIKE %:keyword%")
    Page<Property> searchByKeyword(String keyword, Pageable pageable);

}
