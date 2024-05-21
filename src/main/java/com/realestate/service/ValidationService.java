package com.realestate.service;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.validation.Validator;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private final Validator validator;


    @Transactional
    public <T> void validateData(T data) {
        Set<ConstraintViolation<T>> violations = validator.validate(data);
        if (!violations.isEmpty()) {
            throw new ValidationException("Invalid data: " + violations);
        }
    }
}
