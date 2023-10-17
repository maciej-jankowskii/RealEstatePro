package com.realestate.service;

import com.realestate.dto.HousePropertyDto;
import com.realestate.mapper.HouseMapper;
import com.realestate.model.Property.House;
import com.realestate.repository.HouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HouseService {

    private final HouseRepository houseRepository;
    private final HouseMapper houseMapper;

    public HouseService(HouseRepository houseRepository, HouseMapper houseMapper) {
        this.houseRepository = houseRepository;
        this.houseMapper = houseMapper;
    }

    @Transactional
    public HousePropertyDto saveHouse(HousePropertyDto dto) {
        House house = houseMapper.map(dto);
        House saved = houseRepository.save(house);
        return houseMapper.map(saved);
    }

    public Optional<HousePropertyDto> getHouseById(Long id) {
        return houseRepository.findById(id).map(houseMapper::map);
    }

    public List<HousePropertyDto> getAllHouses() {
        List<House> allHouses = (List<House>) houseRepository.findAll();
        List<HousePropertyDto> dtos = allHouses.stream().map(houseMapper::map).collect(Collectors.toList());
        return dtos;
    }

    public List<HousePropertyDto> filterHouses(
            String address,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Double minLandArea,
            Double maxLandArea,
            Double minHouseArea,
            Double maxHouseArea,
            Integer rooms,
            Integer bathrooms,
            Boolean balcony,
            Boolean garage,
            Boolean twoStoryHouse,
            String buildingType,
            Integer minYearOfConstruction,
            String standard
    ) {
        List<House> allHouses = (List<House>) houseRepository.findAll();
        return allHouses.stream()
                .filter(house -> (address == null || house.getAddress().contains(address))
                        && (minPrice == null || house.getPrice().compareTo(minPrice) >= 0)
                        && (maxPrice == null || house.getPrice().compareTo(maxPrice) <= 0)
                        && (minLandArea == null || house.getLandArea() >= minLandArea)
                        && (maxLandArea == null || house.getLandArea() <= maxLandArea)
                        && (minHouseArea == null || house.getHouseArea() >= minHouseArea)
                        && (maxHouseArea == null || house.getHouseArea() <= maxHouseArea)
                        && (rooms == null || house.getRooms().equals(rooms))
                        && (bathrooms == null || house.getBathrooms().equals(bathrooms))
                        && (balcony == null || house.getBalcony().equals(balcony))
                        && (garage == null || house.getGarage().equals(garage))
                        && (twoStoryHouse == null || house.getTwoStoryHouse().equals(twoStoryHouse))
                        && (buildingType == null || house.getBuildingType().name().equals(buildingType))
                        && (minYearOfConstruction == null || house.getYearOfConstruction() >= minYearOfConstruction)
                        && (standard == null || house.getStandard().name().equals(standard))
                )
                .map(houseMapper::map)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateHouse(HousePropertyDto dto) {
        House house = houseMapper.map(dto);
        houseRepository.save(house);
    }

    public void deleteHouse(Long id) {
        houseRepository.deleteById(id);
    }
}
