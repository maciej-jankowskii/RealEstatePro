package com.realestate.repository;

import com.realestate.dto.EmployeeDto;
import com.realestate.model.Property.Property;
import com.realestate.model.user.UserEmployee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEmployee, Long> {
    Optional<UserEmployee> findByEmail(String email);
    Boolean existsByEmail(String email);

    Page<UserEmployee> findAll(Pageable pageable);
}
