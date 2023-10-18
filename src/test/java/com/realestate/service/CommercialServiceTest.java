package com.realestate.service;

import com.realestate.dto.CommercialPropertyDto;
import com.realestate.mapper.CommercialMapper;
import com.realestate.model.Property.CommercialProperty;
import com.realestate.repository.CommercialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommercialServiceTest {

    @Mock private CommercialRepository commercialRepository;
    @Mock private CommercialMapper commercialMapper;
    @InjectMocks private CommercialService commercialService;
    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveCommercialProperty(){
        CommercialPropertyDto dto = new CommercialPropertyDto();
        CommercialProperty property = new CommercialProperty();

        when(commercialMapper.map(dto)).thenReturn(property);
        when(commercialRepository.save(property)).thenReturn(property);
        when(commercialMapper.map(property)).thenReturn(dto);

        CommercialPropertyDto result = commercialService.saveCommercialProperty(dto);

        assertNotNull(result);
        assertEquals(dto, result);
    }

    @Test
    public void testGetCommercialById(){
        CommercialProperty commercialProperty = new CommercialProperty();
        CommercialPropertyDto dto = new CommercialPropertyDto();
        Long propertyId = 1L;

        when(commercialRepository.findById(propertyId)).thenReturn(Optional.of(commercialProperty));
        when(commercialMapper.map(commercialProperty)).thenReturn(dto);

        Optional<CommercialPropertyDto> result = commercialService.getCommercialPropertyById(propertyId);

        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
    }

    @Test
    public void testGetAllCommercialProperties(){
        List<CommercialProperty> commercialProperties = new ArrayList<>();
        commercialProperties.add(new CommercialProperty());
        commercialProperties.add(new CommercialProperty());
        List<CommercialPropertyDto> dtos = new ArrayList<>();
        dtos.add(new CommercialPropertyDto());
        dtos.add(new CommercialPropertyDto());

        when(commercialRepository.findAll()).thenReturn(commercialProperties);
        when(commercialMapper.map(any(CommercialProperty.class))).thenReturn(dtos.get(0), dtos.get(1));

        List<CommercialPropertyDto> allProperties = commercialService.getAllCommercialProperty();

        assertNotNull(allProperties);
        assertEquals(dtos.size(), allProperties.size());
    }

    @Test
    public void testUpdateCommercialProperty(){
        CommercialPropertyDto dto = new CommercialPropertyDto();
        CommercialProperty property = new CommercialProperty();

        when(commercialMapper.map(dto)).thenReturn(property);
        when(commercialRepository.save(property)).thenReturn(property);

        commercialService.updateCommercialProperty(dto);

        verify(commercialRepository, times(1)).save(property);
    }

    @Test
    public void testDeleteCommercialProperty(){
        Long propertyId = 1L;

        commercialService.deleteCommercialProperty(propertyId);

        verify(commercialRepository, times(1)).deleteById(propertyId);
    }

    @Test
    public void testDeleteNoExistingCommercialProperty(){
        Long propertyId = 1L;

        doThrow(new EmptyResultDataAccessException(1)).when(commercialRepository).deleteById(propertyId);
        assertThrows(EmptyResultDataAccessException.class, () -> commercialService.deleteCommercialProperty(propertyId));
    }

    @Test
    public void testFilterCommercialProperty(){
        List<CommercialProperty> commercialProperties = new ArrayList<>();

        when(commercialRepository.findAll()).thenReturn(commercialProperties);

        List<CommercialPropertyDto> dtos = commercialService.filterCommercialProperties("Example", new BigDecimal("1000"), 50.0,
                150.0, 4, 1, "SKYSCRAPER", 40, "SERVICES");

        assertNotNull(dtos);
        assertEquals(commercialProperties.size(), dtos.size());
    }


}