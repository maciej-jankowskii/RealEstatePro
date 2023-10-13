package com.realestate.service;

import com.realestate.dto.ApartmentPropertyDto;
import com.realestate.mapper.ApartmentMapper;
import com.realestate.model.Property.Apartment;
import com.realestate.repository.ApartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final ApartmentMapper apartmentMapper;

    public ApartmentService(ApartmentRepository apartmentRepository, ApartmentMapper apartmentMapper) {
        this.apartmentRepository = apartmentRepository;
        this.apartmentMapper = apartmentMapper;
    }

    @Transactional
    public ApartmentPropertyDto saveApartment(ApartmentPropertyDto apartmentDto){
        Apartment apartment = apartmentMapper.map(apartmentDto);
        Apartment saved = apartmentRepository.save(apartment);
        return apartmentMapper.map(saved);
    }

    public Optional<ApartmentPropertyDto> getCompanyById(Long id){
        return apartmentRepository.findById(id).map(apartmentMapper::map);
    }
}
