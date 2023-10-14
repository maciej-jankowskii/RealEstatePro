package com.realestate.service;

import com.realestate.dto.ApartmentPropertyDto;
import com.realestate.mapper.ApartmentMapper;
import com.realestate.model.Property.Apartment;
import com.realestate.repository.ApartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public Optional<ApartmentPropertyDto> getApartmentById(Long id){
        return apartmentRepository.findById(id).map(apartmentMapper::map);
    }

    public List<ApartmentPropertyDto> getAllApartments(){
        List<Apartment> apartments = (List<Apartment>) apartmentRepository.findAll();
        List<ApartmentPropertyDto> dtos = apartments.stream().map(apartmentMapper::map).collect(Collectors.toList());
        return dtos;
    }

    public void updateApartment(ApartmentPropertyDto dto){
        Apartment apartment = apartmentMapper.map(dto);
        apartmentRepository.save(apartment);
    }
}
