package com.realestate.service;

import com.realestate.dto.ApartmentPropertyDto;
import com.realestate.enums.BuildingType;
import com.realestate.enums.Standard;
import com.realestate.exceptions.CannotDeleteResourceException;
import com.realestate.exceptions.ResourceNotFoundException;
import com.realestate.mapper.ApartmentMapper;
import com.realestate.model.Property.Apartment;
import com.realestate.repository.ApartmentRepository;
import com.realestate.repository.OffersRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final ApartmentMapper apartmentMapper;
    private final OffersRepository offersRepository;

    public ApartmentService(ApartmentRepository apartmentRepository, ApartmentMapper apartmentMapper, OffersRepository offersRepository) {
        this.apartmentRepository = apartmentRepository;
        this.apartmentMapper = apartmentMapper;
        this.offersRepository = offersRepository;
    }

    @Transactional
    public ApartmentPropertyDto saveApartment(ApartmentPropertyDto apartmentDto) {
        Apartment apartment = apartmentMapper.map(apartmentDto);
        Apartment saved = apartmentRepository.save(apartment);
        return apartmentMapper.map(saved);
    }

    public ApartmentPropertyDto getApartmentById(Long id) {
        return apartmentRepository.findById(id).map(apartmentMapper::map)
                .orElseThrow(() -> new ResourceNotFoundException("Apartment not found"));
    }

//    public List<ApartmentPropertyDto> filterApartments(String address,
//                                                       BigDecimal maxPrice,
//                                                       Double minArea,
//                                                       Double maxArea,
//                                                       Integer rooms,
//                                                       Integer bathrooms,
//                                                       Boolean duplexApartment,
//                                                       String buildingType,
//                                                       Integer maxFloor,
//                                                       Boolean elevator,
//                                                       Boolean balcony,
//                                                       Boolean garage,
//                                                       Integer minYearOfConstruction,
//                                                       String standard) {
//        List<Apartment> allApartments = (List<Apartment>) apartmentRepository.findAll();
//
//        return allApartments.stream()
//                .filter(apartment -> (address == null || apartment.getAddress().contains(address))
//                        && (maxPrice == null || apartment.getPrice().compareTo(maxPrice) <= 0)
//                        && (minArea == null || apartment.getArea() >= minArea)
//                        && (maxArea == null || apartment.getArea() <= maxArea)
//                        && (rooms == null || apartment.getRooms().equals(rooms))
//                        && (bathrooms == null || apartment.getBathrooms().equals(bathrooms))
//                        && (duplexApartment == null || apartment.getDuplexApartment().equals(duplexApartment))
//                        && (buildingType == null || apartment.getBuildingType().name().equals(buildingType))
//                        && (maxFloor == null || apartment.getFloor() <= maxFloor)
//                        && (elevator == null || apartment.getElevator().equals(elevator))
//                        && (balcony == null || apartment.getBalcony().equals(balcony))
//                        && (garage == null || apartment.getGarage().equals(garage))
//                        && (minYearOfConstruction == null || apartment.getYearOfConstruction() >= minYearOfConstruction)
//                        && (standard == null || apartment.getStandard().name().equals(standard)))
//                .map(apartmentMapper::map)
//                .collect(Collectors.toList());
//    }

    public List<ApartmentPropertyDto> getAllApartments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Apartment> apartmentsPage = apartmentRepository.findAll(pageable);
        List<ApartmentPropertyDto> apartmentDtos = apartmentsPage.getContent().stream()
                .map(apartmentMapper::map)
                .collect(Collectors.toList());
        return apartmentDtos;
    }

    @Transactional
    public void updateApartment(Long id, ApartmentPropertyDto updateDto) {
        Apartment apartment = apartmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Apartment not found"));
        updateApartment(updateDto, apartment);
        apartmentRepository.save(apartment);
    }



    public void deleteApartment(Long id) {
        Apartment apartment = apartmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Apartment not found"));
        if (offersRepository.existsByPropertyId(apartment.getId())){
            throw new CannotDeleteResourceException("Cannot remove an apartment assigned to an offer.");
        }
        apartmentRepository.deleteById(id);
    }



    /**

     HELPER METHODS FOR REGISTER

     **/


    private static void updateApartment(ApartmentPropertyDto updateDto, Apartment apartment) {
        apartment.setAddress(updateDto.getAddress());
        apartment.setPrice(updateDto.getPrice());
        apartment.setDescription(updateDto.getDescription());
        apartment.setArea(updateDto.getArea());
        apartment.setRooms(updateDto.getRooms());
        apartment.setBathrooms(updateDto.getBathrooms());
        apartment.setDuplexApartment(updateDto.getDuplexApartment());
        apartment.setBuildingType(BuildingType.valueOf(updateDto.getBuildingType()));
        apartment.setFloor(updateDto.getFloor());
        apartment.setElevator(updateDto.getElevator());
        apartment.setBalcony(updateDto.getBalcony());
        apartment.setGarage(updateDto.getGarage());
        apartment.setYearOfConstruction(updateDto.getYearOfConstruction());
        apartment.setStandard(Standard.valueOf(updateDto.getStandard()));
    }
}
