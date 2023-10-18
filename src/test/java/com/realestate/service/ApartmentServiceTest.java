package com.realestate.service;

import com.realestate.dto.ApartmentPropertyDto;
import com.realestate.mapper.ApartmentMapper;
import com.realestate.model.Property.Apartment;
import com.realestate.repository.ApartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    @InjectMocks private ApartmentService apartmentService;
    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveApartment(){
        ApartmentPropertyDto dto = new ApartmentPropertyDto();
        Apartment apartment = new Apartment();

        when(apartmentMapper.map(dto)).thenReturn(apartment);
        when(apartmentRepository.save(apartment)).thenReturn(apartment);
        when(apartmentMapper.map(apartment)).thenReturn(dto);

        ApartmentPropertyDto result = apartmentService.saveApartment(dto);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto, result);
    }

    @Test
    public void testGetApartmentById(){
        Apartment apartment = new Apartment();
        ApartmentPropertyDto dto = new ApartmentPropertyDto();
        Long apartmentId = 1L;

        when(apartmentRepository.findById(apartmentId)).thenReturn(Optional.of(apartment));
        when(apartmentMapper.map(apartment)).thenReturn(dto);

        Optional<ApartmentPropertyDto> result = apartmentService.getApartmentById(apartmentId);

        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
    }

    @Test
    public void testGetAllApartments(){
        List<Apartment> apartments = new ArrayList<>();
        apartments.add(new Apartment());
        apartments.add(new Apartment());
        List<ApartmentPropertyDto> dtos = new ArrayList<>();
        dtos.add(new ApartmentPropertyDto());
        dtos.add(new ApartmentPropertyDto());

        when(apartmentRepository.findAll()).thenReturn(apartments);
        when(apartmentMapper.map(any(Apartment.class))).thenReturn(dtos.get(0), dtos.get(1));

        List<ApartmentPropertyDto> allApartments = apartmentService.getAllApartments();

        assertNotNull(allApartments);
        assertEquals(apartments.size(), allApartments.size());
    }

    @Test
    public void testUpdateApartment(){
        ApartmentPropertyDto dto = new ApartmentPropertyDto();
        Apartment apartment = new Apartment();

        when(apartmentMapper.map(dto)).thenReturn(apartment);
        when(apartmentRepository.save(apartment)).thenReturn(apartment);

        apartmentService.updateApartment(dto);

        verify(apartmentRepository, times(1)).save(apartment);
    }

    @Test
    public void testDeleteApartment(){
        Long apartmentId = 1L;

        apartmentService.deleteApartment(apartmentId);

        verify(apartmentRepository, times(1)).deleteById(apartmentId);
    }

    @Test
    public void testFilterApartments(){
        List<Apartment> apartments = new ArrayList<>();

        when(apartmentRepository.findAll()).thenReturn(apartments);

        List<ApartmentPropertyDto> dtos = apartmentService.filterApartments("Example", new BigDecimal("900000"), 80.0, 120.0,
                3, 2, false, "TENEMENT", 3, true, true,
                false, 2000, "HIGH_STANDARD");

        assertNotNull(dtos);
        assertEquals(apartments.size(), dtos.size());
    }
}