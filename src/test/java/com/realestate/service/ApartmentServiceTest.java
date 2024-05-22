package com.realestate.service;

import com.realestate.dto.ApartmentPropertyDto;
import com.realestate.enums.BuildingType;
import com.realestate.enums.Standard;
import com.realestate.exceptions.ResourceNotFoundException;
import com.realestate.mapper.ApartmentMapper;
import com.realestate.model.Property.Apartment;
import com.realestate.repository.ApartmentRepository;
import com.realestate.repository.OffersRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ApartmentServiceTest {
    @Mock private ApartmentRepository apartmentRepository;
    @Mock private ApartmentMapper apartmentMapper;
    @Mock private OffersRepository offersRepository;
    @Mock private ValidationService validationService;
    @InjectMocks private ApartmentService apartmentService;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveApartment_ValidDto_ShouldSaveApartmentAndReturnDto() {

        ApartmentPropertyDto inputDto = new ApartmentPropertyDto();
        Apartment apartment = new Apartment();
        Apartment savedApartment = new Apartment();
        ApartmentPropertyDto expectedDto = new ApartmentPropertyDto();

        when(apartmentMapper.map(inputDto)).thenReturn(apartment);
        when(apartmentRepository.save(apartment)).thenReturn(savedApartment);
        when(apartmentMapper.map(savedApartment)).thenReturn(expectedDto);
        doNothing().when(validationService).validateData(any());


        ApartmentPropertyDto resultDto = apartmentService.saveApartment(inputDto);


        assertNotNull(resultDto);
        assertEquals(expectedDto, resultDto);
        verify(apartmentMapper, times(1)).map(inputDto);
        verify(apartmentRepository, times(1)).save(apartment);
        verify(apartmentMapper, times(1)).map(savedApartment);
        verify(validationService, times(1)).validateData(any());
    }

    @Test
    void saveApartment_InvalidDto_ShouldThrowException() {

        ApartmentPropertyDto invalidDto = new ApartmentPropertyDto();
        invalidDto.setPrice(new BigDecimal(-1));

        when(apartmentMapper.map(invalidDto)).thenReturn(new Apartment());
        doThrow(DataIntegrityViolationException.class).when(apartmentRepository).save(any());


        assertThrows(DataIntegrityViolationException.class, () -> apartmentService.saveApartment(invalidDto));

        verify(apartmentMapper, times(1)).map(invalidDto);
        verify(apartmentRepository, times(1)).save(any());
    }

    @Test
    public void getApartmentById_ShouldFindApartment(){
        Apartment apartment = new Apartment();
        ApartmentPropertyDto dto = new ApartmentPropertyDto();
        Long apartmentId = 1L;

        when(apartmentRepository.findById(apartmentId)).thenReturn(Optional.of(apartment));
        when(apartmentMapper.map(apartment)).thenReturn(dto);

        ApartmentPropertyDto result = apartmentService.getApartmentById(apartmentId);

        assertEquals(dto, result);
    }

    @Test
    void getAllApartments_ShouldFindAllApartments() {
        int page = 0;
        int size = 5;
        List<Apartment> apartmentList = new ArrayList<>();
        apartmentList.add(new Apartment());
        apartmentList.add(new Apartment());
        Page<Apartment> apartmentPage = new PageImpl<>(apartmentList);
        when(apartmentRepository.findAll(any(Pageable.class))).thenReturn(apartmentPage);

        List<ApartmentPropertyDto> apartmentDtoList = new ArrayList<>();
        apartmentDtoList.add(new ApartmentPropertyDto());
        apartmentDtoList.add(new ApartmentPropertyDto());
        when(apartmentMapper.map(any(Apartment.class))).thenReturn(new ApartmentPropertyDto());

        List<ApartmentPropertyDto> result = apartmentService.getAllApartments(page, size);

        assertNotNull(result);
        assertEquals(apartmentDtoList.size(), result.size());
        verify(apartmentRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void updateApartment_ValidDto_ShouldUpdateApartment() {
        Long id = 1L;
        ApartmentPropertyDto updateDto = new ApartmentPropertyDto();
        updateDto.setBuildingType(String.valueOf(BuildingType.APARTMENT_BUILDING));
        updateDto.setStandard(String.valueOf(Standard.GOOD_STANDARD));
        Apartment apartment = new Apartment();

        when(apartmentRepository.findById(id)).thenReturn(Optional.of(apartment));
        doNothing().when(validationService).validateData(apartment);
        when(apartmentRepository.save(apartment)).thenReturn(apartment);

        assertDoesNotThrow(() -> apartmentService.updateApartment(id, updateDto));

        verify(apartmentRepository, times(1)).findById(id);
        verify(validationService, times(1)).validateData(apartment);
        verify(apartmentRepository, times(1)).save(apartment);
    }

    @Test
    void updateApartment_InvalidDto_ShouldThrowException() {
        Long id = 1L;
        ApartmentPropertyDto updateDto = new ApartmentPropertyDto();
        updateDto.setPrice(new BigDecimal(-1));
        Apartment apartment = new Apartment();

        when(apartmentRepository.findById(id)).thenReturn(java.util.Optional.of(apartment));
        doThrow(ConstraintViolationException.class).when(validationService).validateData(apartment);

        assertThrows(ConstraintViolationException.class, () -> apartmentService.updateApartment(id, updateDto));

        verify(validationService, times(1)).validateData(apartment);
    }

    @Test
    void updateApartment_ApartmentNotFound_ShouldThrowException() {
        Long id = 1L;
        ApartmentPropertyDto updateDto = new ApartmentPropertyDto();

        when(apartmentRepository.findById(id)).thenReturn(java.util.Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> apartmentService.updateApartment(id, updateDto));

        verify(apartmentRepository, times(1)).findById(id);
    }

    @Test
    public void deleteApartment_ShouldDeleteApartment(){
        Long apartmentId = 1L;

        when(apartmentRepository.findById(apartmentId)).thenReturn(Optional.of(new Apartment()));
        when(offersRepository.existsByPropertyId(anyLong())).thenReturn(false);

        apartmentService.deleteApartment(apartmentId);

        verify(apartmentRepository, times(1)).deleteById(apartmentId);
    }

}