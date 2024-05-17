package com.realestate.service;

import com.realestate.dto.HousePropertyDto;
import com.realestate.enums.BuildingType;
import com.realestate.enums.Standard;
import com.realestate.exceptions.CannotDeleteResourceException;
import com.realestate.exceptions.ResourceNotFoundException;
import com.realestate.mapper.HouseMapper;
import com.realestate.model.Property.House;
import com.realestate.repository.HouseRepository;
import com.realestate.repository.OffersRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HouseService {

    private final HouseRepository houseRepository;
    private final HouseMapper houseMapper;
    private final OffersRepository offersRepository;
    private final ValidationService validationService;

    public HouseService(HouseRepository houseRepository, HouseMapper houseMapper, OffersRepository offersRepository, ValidationService validationService) {
        this.houseRepository = houseRepository;
        this.houseMapper = houseMapper;
        this.offersRepository = offersRepository;
        this.validationService = validationService;
    }

    @Transactional
    public HousePropertyDto saveHouse(@Valid HousePropertyDto dto) {
        House house = houseMapper.map(dto);
        validationService.validateData(house);
        House saved = houseRepository.save(house);
        return houseMapper.map(saved);
    }

    public HousePropertyDto getHouseById(Long id) {
        return houseRepository.findById(id).map(houseMapper::map).orElseThrow(() -> new ResourceNotFoundException("House not found"));
    }

    public List<HousePropertyDto> getAllHouses(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<House> housePage = houseRepository.findAll(pageable);
        List<HousePropertyDto> houseDtos = housePage.getContent().stream()
                .map(houseMapper::map)
                .collect(Collectors.toList());
        return houseDtos;
    }

//    public List<HousePropertyDto> filterHouses(
//            String address,
//            BigDecimal minPrice,
//            BigDecimal maxPrice,
//            Double minLandArea,
//            Double maxLandArea,
//            Double minHouseArea,
//            Double maxHouseArea,
//            Integer rooms,
//            Integer bathrooms,
//            Boolean balcony,
//            Boolean garage,
//            Boolean twoStoryHouse,
//            String buildingType,
//            Integer minYearOfConstruction,
//            String standard
//    ) {
//        List<House> allHouses = (List<House>) houseRepository.findAll();
//        return allHouses.stream()
//                .filter(house -> (address == null || house.getAddress().contains(address))
//                        && (minPrice == null || house.getPrice().compareTo(minPrice) >= 0)
//                        && (maxPrice == null || house.getPrice().compareTo(maxPrice) <= 0)
//                        && (minLandArea == null || house.getLandArea() >= minLandArea)
//                        && (maxLandArea == null || house.getLandArea() <= maxLandArea)
//                        && (minHouseArea == null || house.getHouseArea() >= minHouseArea)
//                        && (maxHouseArea == null || house.getHouseArea() <= maxHouseArea)
//                        && (rooms == null || house.getRooms().equals(rooms))
//                        && (bathrooms == null || house.getBathrooms().equals(bathrooms))
//                        && (balcony == null || house.getBalcony().equals(balcony))
//                        && (garage == null || house.getGarage().equals(garage))
//                        && (twoStoryHouse == null || house.getTwoStoryHouse().equals(twoStoryHouse))
//                        && (buildingType == null || house.getBuildingType().name().equals(buildingType))
//                        && (minYearOfConstruction == null || house.getYearOfConstruction() >= minYearOfConstruction)
//                        && (standard == null || house.getStandard().name().equals(standard))
//                )
//                .map(houseMapper::map)
//                .collect(Collectors.toList());
//    }

    @Transactional
    public void updateHouse(Long id, @Valid HousePropertyDto dto) {
        House house = houseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("House not found"));
        validationService.validateData(house);
        updateHouse(dto, house);
        houseRepository.save(house);
    }

    public void deleteHouse(Long id) {
        House house = houseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("House not found"));
        if (offersRepository.existsByPropertyId(house.getId())){
            throw new CannotDeleteResourceException("Cannot delete house assigned to offer");
        }
        houseRepository.deleteById(id);
    }



    private static void updateHouse(HousePropertyDto dto, House house) {
        house.setAddress(dto.getAddress());
        house.setPrice(dto.getPrice());
        house.setDescription(dto.getDescription());
        house.setLandArea(dto.getLandArea());
        house.setHouseArea(dto.getHouseArea());
        house.setRooms(dto.getRooms());
        house.setBathrooms(dto.getBathrooms());
        house.setBalcony(dto.getBalcony());
        house.setGarage(dto.getGarage());
        house.setTwoStoryHouse(dto.getTwoStoryHouse());
        house.setBuildingType(BuildingType.valueOf(dto.getBuildingType()));
        house.setYearOfConstruction(dto.getYearOfConstruction());
        house.setStandard(Standard.valueOf(dto.getStandard()));
    }
}
