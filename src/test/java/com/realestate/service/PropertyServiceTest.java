package com.realestate.service;

import com.realestate.dto.PropertyDto;
import com.realestate.mapper.PropertyMapper;
import com.realestate.model.Property.Property;
import com.realestate.repository.PropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PropertyServiceTest {

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private PropertyMapper propertyMapper;

    @InjectMocks
    private PropertyService propertyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllProperties_ShouldReturnListOfProperties() {
        int page = 0;
        int size = 10;
        List<Property> propertyList = new ArrayList<>();
        propertyList.add(new Property());
        propertyList.add(new Property());
        Page<Property> propertyPage = mock(Page.class);
        when(propertyPage.getContent()).thenReturn(propertyList);
        when(propertyRepository.findAll(any(Pageable.class))).thenReturn(propertyPage);

        List<PropertyDto> propertyDtoList = new ArrayList<>();
        propertyDtoList.add(new PropertyDto());
        propertyDtoList.add(new PropertyDto());
        when(propertyMapper.map(any(Property.class))).thenReturn(new PropertyDto());

        List<PropertyDto> result = propertyService.getAllProperties(page, size);

        assertEquals(propertyDtoList.size(), result.size());
        verify(propertyRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void searchProperties_ShouldReturnListOfProperties() {
        String keyword = "house";
        int page = 0;
        int size = 10;
        List<Property> propertyList = new ArrayList<>();
        propertyList.add(new Property());
        propertyList.add(new Property());
        Page<Property> propertyPage = mock(Page.class);
        when(propertyPage.getContent()).thenReturn(propertyList);
        when(propertyRepository.searchByKeyword(eq(keyword), any(Pageable.class))).thenReturn(propertyPage);

        List<PropertyDto> propertyDtoList = new ArrayList<>();
        propertyDtoList.add(new PropertyDto());
        propertyDtoList.add(new PropertyDto());
        when(propertyMapper.map(any(Property.class))).thenReturn(new PropertyDto());

        List<PropertyDto> result = propertyService.searchProperties(keyword, page, size);

        assertEquals(propertyDtoList.size(), result.size());
        verify(propertyRepository, times(1)).searchByKeyword(eq(keyword), any(Pageable.class));
    }
}
