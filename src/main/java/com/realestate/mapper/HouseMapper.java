package com.realestate.mapper;

import com.realestate.dto.HousePropertyDto;
import com.realestate.enums.BuildingType;
import com.realestate.enums.Standard;
import com.realestate.model.Property.House;
import org.springframework.stereotype.Service;

@Service
public class HouseMapper {

    public HousePropertyDto map(House house){
        HousePropertyDto dto = new HousePropertyDto();
        dto.setId(house.getId());
        dto.setAddress(house.getAddress());
        dto.setPrice(house.getPrice());
        dto.setDescription(house.getDescription());
        dto.setLandArea(house.getLandArea());
        dto.setHouseArea(house.getHouseArea());
        dto.setRooms(house.getRooms());
        dto.setBathrooms(house.getBathrooms());
        dto.setBalcony(house.getBalcony());
        dto.setGarage(house.getGarage());
        dto.setTwoStoryHouse(house.getTwoStoryHouse());
        dto.setBuildingType(String.valueOf(house.getBuildingType()));
        dto.setYearOfConstruction(house.getYearOfConstruction());
        dto.setStandard(String.valueOf(house.getStandard()));
        return dto;
    }

    public House map(HousePropertyDto dto){
        House house = new House();
        house.setId(dto.getId());
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
        return house;
    }
}
