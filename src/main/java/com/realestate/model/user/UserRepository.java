package com.realestate.model.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEmployee, Long> {

    Optional<UserEmployee> findByEmail(String email);
    Boolean existsByEmail(String email);
}
