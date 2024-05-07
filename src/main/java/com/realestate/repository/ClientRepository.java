package com.realestate.repository;

import com.realestate.model.Property.Apartment;
import com.realestate.model.client.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {

    Page<Client> findAll(Pageable pageable);
}
