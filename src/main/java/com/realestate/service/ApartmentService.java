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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final ApartmentMapper apartmentMapper;
    private final OffersRepository offersRepository;
    private final ValidationService validationService;


    @Transactional
    public ApartmentPropertyDto saveApartment(@Valid ApartmentPropertyDto apartmentDto) {
        Apartment apartment = apartmentMapper.map(apartmentDto);
        validationService.validateData(apartment);
        Apartment saved = apartmentRepository.save(apartment);
        return apartmentMapper.map(saved);
    }

    public ApartmentPropertyDto getApartmentById(Long id) {
        return apartmentRepository.findById(id).map(apartmentMapper::map)
                .orElseThrow(() -> new ResourceNotFoundException("Apartment not found"));
    }


    public List<ApartmentPropertyDto> getAllApartments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Apartment> apartmentsPage = apartmentRepository.findAll(pageable);
        return apartmentsPage.getContent().stream()
                .map(apartmentMapper::map)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateApartment(Long id, @Valid ApartmentPropertyDto updateDto) {
        Apartment apartment = apartmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Apartment not found"));
        validationService.validateData(apartment);
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

     HELPER METHODS FOR UPDATE

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
