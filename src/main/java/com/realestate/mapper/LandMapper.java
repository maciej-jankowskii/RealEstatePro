package com.realestate.mapper;

import com.realestate.dto.LandPropertyDto;
import com.realestate.enums.TypeOfLand;
import com.realestate.model.Property.Land;
import org.springframework.stereotype.Service;

@Service
public class LandMapper {

    public LandPropertyDto map(Land land) {
        LandPropertyDto dto = new LandPropertyDto();
        dto.setId(land.getId());
        dto.setAddress(land.getAddress());
        dto.setPrice(land.getPrice());
        dto.setDescription(land.getDescription());
        dto.setTypeOfLand(String.valueOf(land.getTypeOfLand()));
        dto.setArea(land.getArea());
        dto.setBuildingPermit(land.getBuildingPermit());
        return dto;
    }

    public Land map(LandPropertyDto dto) {
        Land land = new Land();
        land.setId(dto.getId());
        land.setAddress(dto.getAddress());
        land.setPrice(dto.getPrice());
        land.setDescription(dto.getDescription());
        land.setTypeOfLand(TypeOfLand.valueOf(dto.getTypeOfLand()));
        land.setArea(dto.getArea());
        land.setBuildingPermit(dto.getBuildingPermit());
        return land;
    }
}
