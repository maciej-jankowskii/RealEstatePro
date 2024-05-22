package com.realestate.service;

import com.realestate.dto.HousePropertyDto;
import com.realestate.enums.BuildingType;
import com.realestate.enums.Standard;
import com.realestate.exceptions.ResourceNotFoundException;
import com.realestate.mapper.HouseMapper;
import com.realestate.model.Property.House;
import com.realestate.repository.HouseRepository;
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

class HouseServiceTest {

    @Mock
    private HouseRepository houseRepository;

    @Mock
    private HouseMapper houseMapper;
    @Mock
    private OffersRepository offersRepository;
    @Mock
    private ValidationService validationService;

    @InjectMocks
    private HouseService houseService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveHouse_ValidDto_ShouldSaveHouseAndReturnDto() {
        HousePropertyDto inputDto = new HousePropertyDto();
        House house = new House();
        House savedHouse = new House();
        HousePropertyDto expectedDto = new HousePropertyDto();

        when(houseMapper.map(inputDto)).thenReturn(house);
        when(houseRepository.save(house)).thenReturn(savedHouse);
        when(houseMapper.map(savedHouse)).thenReturn(expectedDto);
        doNothing().when(validationService).validateData(any());

        HousePropertyDto resultDto = houseService.saveHouse(inputDto);

        assertNotNull(resultDto);
        assertEquals(expectedDto, resultDto);
        verify(houseMapper, times(1)).map(inputDto);
        verify(houseRepository, times(1)).save(house);
        verify(houseMapper, times(1)).map(savedHouse);
        verify(validationService, times(1)).validateData(any());
    }

    @Test
    void saveHouse_InvalidDto_ShouldThrowException() {
        HousePropertyDto invalidDto = new HousePropertyDto();
        invalidDto.setPrice(new BigDecimal(-1));

        when(houseMapper.map(invalidDto)).thenReturn(new House());
        doThrow(DataIntegrityViolationException.class).when(houseRepository).save(any());

        assertThrows(DataIntegrityViolationException.class, () -> houseService.saveHouse(invalidDto));

        verify(houseMapper, times(1)).map(invalidDto);
        verify(houseRepository, times(1)).save(any());
    }

    @Test
    public void getHouseById_ShouldFindHouse() {
        HousePropertyDto dto = new HousePropertyDto();
        House house = new House();
        Long houseId = 1L;

        when(houseRepository.findById(houseId)).thenReturn(Optional.of(house));
        when(houseMapper.map(house)).thenReturn(dto);

        HousePropertyDto result = houseService.getHouseById(houseId);

        assertNotNull(result);
        assertEquals(dto, result);
    }

    @Test
    void getAllHouses_ShouldFindAllHouses() {
        int page = 0;
        int size = 10;
        List<House> houseList = new ArrayList<>();
        houseList.add(new House());
        houseList.add(new House());
        Page<House> housePage = new PageImpl<>(houseList);
        when(houseRepository.findAll(any(Pageable.class))).thenReturn(housePage);

        List<HousePropertyDto> houseDtoList = new ArrayList<>();
        houseDtoList.add(new HousePropertyDto());
        houseDtoList.add(new HousePropertyDto());
        when(houseMapper.map(any(House.class))).thenReturn(new HousePropertyDto());

        List<HousePropertyDto> result = houseService.getAllHouses(page, size);

        assertNotNull(result);
        assertEquals(houseDtoList.size(), result.size());
        verify(houseRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void updateHouse_HouseFound_ShouldUpdateHouse() {
        Long id = 1L;
        HousePropertyDto updateDto = new HousePropertyDto();
        updateDto.setStandard(String.valueOf(Standard.GOOD_STANDARD));
        updateDto.setBuildingType(String.valueOf(BuildingType.MULTI_FAMILY_HOUSE));
        House house = new House();


        when(houseRepository.findById(id)).thenReturn(Optional.of(house));
        doNothing().when(validationService).validateData(house);
        when(houseRepository.save(house)).thenReturn(house);

        assertDoesNotThrow(() -> houseService.updateHouse(id, updateDto));

        verify(houseRepository, times(1)).findById(id);
        verify(validationService, times(1)).validateData(house);
        verify(houseRepository, times(1)).save(house);
    }

    @Test
    void updateHouse_HouseNotFound_ShouldThrowResourceNotFoundException() {
        Long id = 1L;

        when(houseRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> houseService.updateHouse(id, new HousePropertyDto()));

        verify(houseRepository, times(1)).findById(id);
        verifyNoInteractions(validationService);
        verifyNoMoreInteractions(houseRepository);
    }

    @Test
    public void deleteHouse_ShouldDelete() {
        Long houseId = 1L;
        when(houseRepository.findById(houseId)).thenReturn(Optional.of(new House()));
        when(offersRepository.existsByPropertyId(houseId)).thenReturn(false);
        houseService.deleteHouse(houseId);

        verify(houseRepository, times(1)).deleteById(houseId);
    }

    @Test
    public void deleteHouse_ShouldNotFoundHouse(){
        Long houseId = 1L;

        doThrow(new ResourceNotFoundException("House not found")).when(houseRepository).deleteById(houseId);
        assertThrows(ResourceNotFoundException.class, () -> houseService.deleteHouse(houseId));
    }

}
