package com.realestate.service;

import com.realestate.dto.LandPropertyDto;
import com.realestate.enums.TypeOfLand;
import com.realestate.exceptions.ResourceNotFoundException;
import com.realestate.mapper.LandMapper;
import com.realestate.model.Property.Land;
import com.realestate.repository.LandRepository;
import com.realestate.repository.OffersRepository;
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

class LandServiceTest {

    @Mock
    private LandRepository landRepository;

    @Mock
    private LandMapper landMapper;
    @Mock
    private OffersRepository offersRepository;
    @Mock
    private ValidationService validationService;

    @InjectMocks
    private LandService landService;


    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveLand_ValidDto_ShouldSaveLandAndReturnDto() {
        LandPropertyDto inputDto = new LandPropertyDto();
        Land land = new Land();
        Land savedLand = new Land();
        LandPropertyDto expectedDto = new LandPropertyDto();

        when(landMapper.map(inputDto)).thenReturn(land);
        when(landRepository.save(land)).thenReturn(savedLand);
        when(landMapper.map(savedLand)).thenReturn(expectedDto);
        doNothing().when(validationService).validateData(any());

        LandPropertyDto resultDto = landService.saveLand(inputDto);

        assertNotNull(resultDto);
        assertEquals(expectedDto, resultDto);
        verify(landMapper, times(1)).map(inputDto);
        verify(landRepository, times(1)).save(land);
        verify(landMapper, times(1)).map(savedLand);
        verify(validationService, times(1)).validateData(any());
    }

    @Test
    void saveLand_InvalidDto_ShouldThrowException() {
        LandPropertyDto invalidDto = new LandPropertyDto();
        invalidDto.setPrice(new BigDecimal(-1));

        when(landMapper.map(invalidDto)).thenReturn(new Land());
        doThrow(DataIntegrityViolationException.class).when(landRepository).save(any());

        assertThrows(DataIntegrityViolationException.class, () -> landService.saveLand(invalidDto));

        verify(landMapper, times(1)).map(invalidDto);
        verify(landRepository, times(1)).save(any());
    }

    @Test
    public void getLandById_ShouldFindLand() {
        LandPropertyDto dto = new LandPropertyDto();
        Land land = new Land();
        Long landId = 1L;

        when(landRepository.findById(landId)).thenReturn(Optional.of(land));
        when(landMapper.map(land)).thenReturn(dto);

        LandPropertyDto result = landService.getLandById(landId);

        assertNotNull(result);
        assertEquals(dto, result);
    }

    @Test
    void getAllLands_ShouldFindAllLands() {

        int page = 0;
        int size = 10;
        List<Land> landList = new ArrayList<>();
        landList.add(new Land());
        landList.add(new Land());
        Page<Land> landPage = new PageImpl<>(landList);
        when(landRepository.findAll(any(Pageable.class))).thenReturn(landPage);

        List<LandPropertyDto> landDtoList = new ArrayList<>();
        landDtoList.add(new LandPropertyDto());
        landDtoList.add(new LandPropertyDto());
        when(landMapper.map(any(Land.class))).thenReturn(new LandPropertyDto());

        List<LandPropertyDto> result = landService.getAllLands(page, size);

        assertNotNull(result);
        assertEquals(landDtoList.size(), result.size());
        verify(landRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void updateLand_LandFound_ShouldUpdateLand() {
        Long id = 1L;
        LandPropertyDto updateDto = new LandPropertyDto();
        updateDto.setTypeOfLand(String.valueOf(TypeOfLand.RECREATIONAL_LAND));
        Land land = new Land();

        when(landRepository.findById(id)).thenReturn(Optional.of(land));
        doNothing().when(validationService).validateData(land);
        when(landRepository.save(land)).thenReturn(land);

        assertDoesNotThrow(() -> landService.updateLand(id, updateDto));

        verify(landRepository, times(1)).findById(id);
        verify(validationService, times(1)).validateData(land);
        verify(landRepository, times(1)).save(land);
    }

    @Test
    void updateLand_LandNotFound_ShouldThrowResourceNotFoundException() {
        Long id = 1L;

        when(landRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> landService.updateLand(id, new LandPropertyDto()));

        verify(landRepository, times(1)).findById(id);
        verifyNoInteractions(validationService);
        verifyNoMoreInteractions(landRepository);
    }

    @Test
    public void deleteLand_ShouldDelete() {

        Long houseId = 1L;

        Long landId = 1L;

        when(landRepository.findById(houseId)).thenReturn(Optional.of(new Land()));
        when(offersRepository.existsByPropertyId(houseId)).thenReturn(false);

        landService.deleteLand(landId);

        verify(landRepository, times(1)).deleteById(landId);
    }
}
