package com.realestate.service;

import com.realestate.dto.ApartmentPropertyDto;
import com.realestate.mapper.ApartmentMapper;
import com.realestate.model.Property.Apartment;
import com.realestate.repository.ApartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    public ApartmentPropertyDto saveApartment(ApartmentPropertyDto apartmentDto) {
        Apartment apartment = apartmentMapper.map(apartmentDto);
        Apartment saved = apartmentRepository.save(apartment);
        return apartmentMapper.map(saved);
    }

    public Optional<ApartmentPropertyDto> getApartmentById(Long id) {
        return apartmentRepository.findById(id).map(apartmentMapper::map);
    }

    public List<ApartmentPropertyDto> filterApartments(String address,
                                                       BigDecimal maxPrice,
                                                       Double minArea,
                                                       Double maxArea,
                                                       Integer rooms,
                                                       Integer bathrooms,
                                                       Boolean duplexApartment,
                                                       String buildingType,
                                                       Integer maxFloor,
                                                       Boolean elevator,
                                                       Boolean balcony,
                                                       Boolean garage,
                                                       Integer minYearOfConstruction,
                                                       String standard) {
        List<Apartment> allApartments = (List<Apartment>) apartmentRepository.findAll();

        return allApartments.stream()
                .filter(apartment -> (address == null || apartment.getAddress().contains(address))
                        && (maxPrice == null || apartment.getPrice().compareTo(maxPrice) <= 0)
                        && (minArea == null || apartment.getArea() >= minArea)
                        && (maxArea == null || apartment.getArea() <= maxArea)
                        && (rooms == null || apartment.getRooms().equals(rooms))
                        && (bathrooms == null || apartment.getBathrooms().equals(bathrooms))
                        && (duplexApartment == null || apartment.getDuplexApartment().equals(duplexApartment))
                        && (buildingType == null || apartment.getBuildingType().name().equals(buildingType))
                        && (maxFloor == null || apartment.getFloor() <= maxFloor)
                        && (elevator == null || apartment.getElevator().equals(elevator))
                        && (balcony == null || apartment.getBalcony().equals(balcony))
                        && (garage == null || apartment.getGarage().equals(garage))
                        && (minYearOfConstruction == null || apartment.getYearOfConstruction() >= minYearOfConstruction)
                        && (standard == null || apartment.getStandard().name().equals(standard)))
                .map(apartmentMapper::map)
                .collect(Collectors.toList());
    }

    public List<ApartmentPropertyDto> getAllApartments() {
        List<Apartment> apartments = (List<Apartment>) apartmentRepository.findAll();
        List<ApartmentPropertyDto> dtos = apartments.stream().map(apartmentMapper::map).collect(Collectors.toList());
        return dtos;
    }

    @Transactional
    public void updateApartment(ApartmentPropertyDto dto) {
        Apartment apartment = apartmentMapper.map(dto);
        apartmentRepository.save(apartment);
    }

    public void deleteApartment(Long id) {
        apartmentRepository.deleteById(id);
    }
}
