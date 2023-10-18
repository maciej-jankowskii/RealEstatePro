package com.realestate.service;

import com.realestate.dto.HousePropertyDto;
import com.realestate.mapper.HouseMapper;
import com.realestate.model.Property.House;
import com.realestate.repository.HouseRepository;
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

class HouseServiceTest {

    @Mock
    private HouseRepository houseRepository;
    @Mock
    private HouseMapper houseMapper;
    @InjectMocks
    private HouseService houseService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveHouse() {
        HousePropertyDto dto = new HousePropertyDto();
        House house = new House();

        when(houseMapper.map(dto)).thenReturn(house);
        when(houseRepository.save(house)).thenReturn(house);
        when(houseMapper.map(house)).thenReturn(dto);

        HousePropertyDto result = houseService.saveHouse(dto);

        assertNotNull(result);
        assertEquals(dto, result);
    }

    @Test
    public void testGetHouseById() {
        HousePropertyDto dto = new HousePropertyDto();
        House house = new House();
        Long houseId = 1L;

        when(houseRepository.findById(houseId)).thenReturn(Optional.of(house));
        when(houseMapper.map(house)).thenReturn(dto);

        Optional<HousePropertyDto> result = houseService.getHouseById(houseId);

        assertTrue(result.isPresent());
        assertEquals(dto, result.get());

    }

    @Test
    public void testGetAllHouses() {
        List<House> houses = new ArrayList<>();
        houses.add(new House());
        houses.add(new House());
        List<HousePropertyDto> dtos = new ArrayList<>();
        dtos.add(new HousePropertyDto());
        dtos.add(new HousePropertyDto());

        when(houseRepository.findAll()).thenReturn(houses);
        when(houseMapper.map(any(House.class))).thenReturn(dtos.get(0), dtos.get(1));

        List<HousePropertyDto> allHouses = houseService.getAllHouses();

        assertNotNull(allHouses);
        assertEquals(dtos.size(), allHouses.size());

    }

    @Test
    public void testUpdateHouse() {
        HousePropertyDto dto = new HousePropertyDto();
        House house = new House();

        when(houseMapper.map(dto)).thenReturn(house);
        when(houseRepository.save(house)).thenReturn(house);

        houseService.updateHouse(dto);

        verify(houseRepository, times(1)).save(house);
    }

    @Test
    public void testDeleteHouse() {
        Long houseId = 1L;

        houseService.deleteHouse(houseId);

        verify(houseRepository, times(1)).deleteById(houseId);
    }

    @Test
    public void testDeleteNoExistHouse(){
        Long houseId = 1L;

        doThrow(new EmptyResultDataAccessException(1)).when(houseRepository).deleteById(houseId);
        assertThrows(EmptyResultDataAccessException.class, () -> houseService.deleteHouse(houseId));
    }

    @Test
    public void testFilterHouses() {
        List<House> houses = new ArrayList<>();

        when(houseRepository.findAll()).thenReturn(houses);

        List<HousePropertyDto> dtos = houseService.filterHouses("Example", new BigDecimal("10000"), new BigDecimal("5000000"),
                100.0, 900.0, 150.0, 220.0, 6, 3,
                true, true, true, "SINGLE_FAMILY_HOUSE",
                2000, "GOOD_STANDARD");

        assertEquals(houses.size(), dtos.size());
        assertNotNull(dtos);


    }

}