package com.realestate.service;

import com.realestate.dto.CommercialPropertyDto;
import com.realestate.enums.BuildingType;
import com.realestate.enums.TypeOfBusiness;
import com.realestate.exceptions.ResourceNotFoundException;
import com.realestate.mapper.CommercialMapper;
import com.realestate.model.Property.CommercialProperty;
import com.realestate.repository.CommercialRepository;
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

class CommercialServiceTest {

    @Mock private CommercialRepository commercialPropertyRepository;
    @Mock private CommercialMapper commercialPropertyMapper;
    @Mock private ValidationService validationService;
    @Mock private OffersRepository offersRepository;
    @InjectMocks private CommercialService commercialPropertyService;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveCommercialProperty_ValidDto_ShouldSavePropertyAndReturnDto() {
        CommercialPropertyDto inputDto = new CommercialPropertyDto();
        CommercialProperty property = new CommercialProperty();
        CommercialProperty savedProperty = new CommercialProperty();
        CommercialPropertyDto expectedDto = new CommercialPropertyDto();

        when(commercialPropertyMapper.map(inputDto)).thenReturn(property);
        when(commercialPropertyRepository.save(property)).thenReturn(savedProperty);
        when(commercialPropertyMapper.map(savedProperty)).thenReturn(expectedDto);
        doNothing().when(validationService).validateData(any());

        CommercialPropertyDto resultDto = commercialPropertyService.saveCommercialProperty(inputDto);

        assertNotNull(resultDto);
        assertEquals(expectedDto, resultDto);
        verify(commercialPropertyMapper, times(1)).map(inputDto);
        verify(commercialPropertyRepository, times(1)).save(property);
        verify(commercialPropertyMapper, times(1)).map(savedProperty);
        verify(validationService, times(1)).validateData(any());
    }

    @Test
    void saveCommercialProperty_InvalidDto_ShouldThrowException() {
        CommercialPropertyDto invalidDto = new CommercialPropertyDto();
        invalidDto.setPrice(new BigDecimal(-1));

        when(commercialPropertyMapper.map(invalidDto)).thenReturn(new CommercialProperty());
        doThrow(DataIntegrityViolationException.class).when(commercialPropertyRepository).save(any());

        assertThrows(DataIntegrityViolationException.class, () -> commercialPropertyService.saveCommercialProperty(invalidDto));

        verify(commercialPropertyMapper, times(1)).map(invalidDto);
        verify(commercialPropertyRepository, times(1)).save(any());
    }

    @Test
    public void getCommercialById_ShouldFindCommercial(){
        CommercialProperty commercialProperty = new CommercialProperty();
        CommercialPropertyDto dto = new CommercialPropertyDto();
        Long propertyId = 1L;

        when(commercialPropertyRepository.findById(propertyId)).thenReturn(Optional.of(commercialProperty));
        when(commercialPropertyMapper.map(commercialProperty)).thenReturn(dto);

        CommercialPropertyDto result = commercialPropertyService.getCommercialPropertyById(propertyId);

        assertNotNull(result);
        assertEquals(dto, result);
    }

    @Test
    void getAllCommercialProperties_ShouldFindAllCommercials() {
        int page = 0;
        int size = 5;
        List<CommercialProperty> commercialPropertyList = new ArrayList<>();
        commercialPropertyList.add(new CommercialProperty());
        commercialPropertyList.add(new CommercialProperty());
        Page<CommercialProperty> commercialPropertyPage = new PageImpl<>(commercialPropertyList);
        when(commercialPropertyRepository.findAll(any(Pageable.class))).thenReturn(commercialPropertyPage);

        List<CommercialPropertyDto> commercialPropertyDtoList = new ArrayList<>();
        commercialPropertyDtoList.add(new CommercialPropertyDto());
        commercialPropertyDtoList.add(new CommercialPropertyDto());
        when(commercialPropertyMapper.map(any(CommercialProperty.class))).thenReturn(new CommercialPropertyDto());

        List<CommercialPropertyDto> result = commercialPropertyService.getAllCommercialProperty(page, size);

        assertNotNull(result);
        assertEquals(commercialPropertyDtoList.size(), result.size());
        verify(commercialPropertyRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void updateCommercialProperty_ValidDto_ShouldUpdateCommercialProperty() {
        Long id = 1L;
        CommercialPropertyDto updateDto = new CommercialPropertyDto();
        updateDto.setTypeOfBusiness(String.valueOf(TypeOfBusiness.SERVICES));
        updateDto.setBuildingType(String.valueOf(BuildingType.APARTMENT_BUILDING));
        CommercialProperty commercialProperty = new CommercialProperty();

        when(commercialPropertyRepository.findById(id)).thenReturn(Optional.of(commercialProperty));

        assertDoesNotThrow(() -> commercialPropertyService.updateCommercialProperty(id, updateDto));

        verify(commercialPropertyRepository, times(1)).findById(id);
        verify(validationService, times(1)).validateData(commercialProperty);
        verify(commercialPropertyRepository, times(1)).save(commercialProperty);
    }

    @Test
    void updateCommercialProperty_InvalidDto_ShouldThrowValidationException() {
        Long id = 1L;
        CommercialPropertyDto updateDto = new CommercialPropertyDto();
        CommercialProperty commercialProperty = new CommercialProperty();

        when(commercialPropertyRepository.findById(id)).thenReturn(Optional.of(commercialProperty));
        doThrow(ConstraintViolationException.class).when(validationService).validateData(commercialProperty);

        assertThrows(ConstraintViolationException.class, () -> commercialPropertyService.updateCommercialProperty(id, updateDto));
        verify(commercialPropertyRepository, times(1)).findById(id);
        verify(validationService, times(1)).validateData(commercialProperty);
        verifyNoMoreInteractions(commercialPropertyRepository);
        verifyNoMoreInteractions(validationService);
    }

    @Test
    void updateCommercialProperty_PropertyNotFound_ShouldThrowResourceNotFoundException() {
        Long id = 1L;
        CommercialPropertyDto updateDto = new CommercialPropertyDto();

        when(commercialPropertyRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> commercialPropertyService.updateCommercialProperty(id, updateDto));
        verify(commercialPropertyRepository, times(1)).findById(id);
        verifyNoInteractions(validationService);
        verifyNoMoreInteractions(commercialPropertyRepository);
    }



    @Test
    void deleteCommercialProperty_PropertyNotFound_ShouldThrowResourceNotFoundException() {
        Long id = 1L;

        when(commercialPropertyRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> commercialPropertyService.deleteCommercialProperty(id));

        verify(commercialPropertyRepository, times(1)).findById(id);
        verifyNoInteractions(offersRepository);
        verifyNoMoreInteractions(commercialPropertyRepository);
    }

}
