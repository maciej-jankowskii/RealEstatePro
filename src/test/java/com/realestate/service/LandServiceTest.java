package com.realestate.service;

import com.realestate.dto.LandPropertyDto;
import com.realestate.mapper.LandMapper;
import com.realestate.model.Property.Land;
import com.realestate.repository.LandRepository;
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

class LandServiceTest {

    @Mock
    private LandRepository landRepository;
    @Mock
    private LandMapper landMapper;
    @InjectMocks
    LandService landService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveLand() {
        LandPropertyDto dto = new LandPropertyDto();
        Land land = new Land();

        when(landMapper.map(dto)).thenReturn(land);
        when(landRepository.save(land)).thenReturn(land);
        when(landMapper.map(land)).thenReturn(dto);

        LandPropertyDto result = landService.saveLand(dto);

        assertEquals(dto, result);
        assertNotNull(result);
    }

    @Test
    public void testGetLandById() {
        LandPropertyDto dto = new LandPropertyDto();
        Land land = new Land();
        Long landId = 1L;

        when(landRepository.findById(landId)).thenReturn(Optional.of(land));
        when(landMapper.map(land)).thenReturn(dto);

        Optional<LandPropertyDto> resultLand = landService.getLandById(landId);

        assertTrue(resultLand.isPresent());
        assertEquals(dto, resultLand.get());

    }

    @Test
    public void testGetAllLands() {
        List<LandPropertyDto> dtos = new ArrayList<>();
        dtos.add(new LandPropertyDto());
        dtos.add(new LandPropertyDto());
        List<Land> lands = new ArrayList<>();
        lands.add(new Land());
        lands.add(new Land());

        when(landRepository.findAll()).thenReturn(lands);
        when(landMapper.map(any(Land.class))).thenReturn(dtos.get(0), dtos.get(1));

        List<LandPropertyDto> allLands = landService.getAllLands();

        assertNotNull(allLands);
        assertEquals(dtos.size(), allLands.size());
    }

    @Test
    public void testUpdateLand() {
        LandPropertyDto dto = new LandPropertyDto();
        Land land = new Land();

        when(landMapper.map(dto)).thenReturn(land);
        when(landRepository.save(land)).thenReturn(land);

        landService.updateLand(dto);

        verify(landRepository, times(1)).save(land);
    }

    @Test
    public void testDeleteLand() {
        Long landId = 1L;

        landService.deleteLand(landId);

        verify(landRepository, times(1)).deleteById(landId);
    }

    @Test
    public void testDeleteNoExistLand() {
        Long landId = 1L;

        doThrow(new EmptyResultDataAccessException(1)).when(landRepository).deleteById(landId);
        assertThrows(EmptyResultDataAccessException.class, () -> landService.deleteLand(landId));
    }

    @Test
    public void testFilterLand() {
        List<Land> lands = new ArrayList<>();

        when(landRepository.findAll()).thenReturn(lands);

        List<LandPropertyDto> resultLand = landService.filterLands("Example", new BigDecimal("1000"), new BigDecimal("500000"),
                "BUILDING_SITE", 500.0, 1100.0, true);

        assertNotNull(resultLand);
        assertEquals(lands.size(), resultLand.size());
    }

}