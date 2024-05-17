package com.realestate.service;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import jakarta.validation.Validator;

import java.util.Set;

@Service
public class ValidationService {

    private final Validator validator;

    public ValidationService(Validator validator) {
        this.validator = validator;
    }

    @Transactional
    public <T> void validateData(T data) {
        Set<ConstraintViolation<T>> violations = validator.validate(data);
        if (!violations.isEmpty()) {
            throw new ValidationException("Invalid data: " + violations);
        }
    }


}
