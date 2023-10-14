package com.realestate.mapper;

import com.realestate.dto.CommercialPropertyDto;
import com.realestate.enums.BuildingType;
import com.realestate.enums.TypeOfBusiness;
import com.realestate.model.Property.CommercialProperty;
import org.springframework.stereotype.Service;

@Service
public class CommercialMapper {

    public CommercialPropertyDto map(CommercialProperty commercialProperty){
        CommercialPropertyDto dto = new CommercialPropertyDto();
        dto.setId(commercialProperty.getId());
        dto.setAddress(commercialProperty.getAddress());
        dto.setPrice(commercialProperty.getPrice());
        dto.setDescription(commercialProperty.getDescription());
        dto.setArea(commercialProperty.getArea());
        dto.setRooms(commercialProperty.getRooms());
        dto.setBathrooms(commercialProperty.getBathrooms());
        dto.setBuildingType(String.valueOf(commercialProperty.getBuildingType()));
        dto.setFloor(commercialProperty.getFloor());
        dto.setTypeOfBusiness(String.valueOf(commercialProperty.getTypeOfBusiness()));
        return dto;
    }

    public CommercialProperty map(CommercialPropertyDto dto){
        CommercialProperty commercialProperty = new CommercialProperty();
        commercialProperty.setId(dto.getId());
        commercialProperty.setAddress(dto.getAddress());
        commercialProperty.setPrice(dto.getPrice());
        commercialProperty.setDescription(dto.getDescription());
        commercialProperty.setArea(dto.getArea());
        commercialProperty.setRooms(dto.getRooms());
        commercialProperty.setBathrooms(dto.getBathrooms());
        commercialProperty.setBuildingType(BuildingType.valueOf(dto.getBuildingType()));
        commercialProperty.setFloor(dto.getFloor());
        commercialProperty.setTypeOfBusiness(TypeOfBusiness.valueOf(dto.getTypeOfBusiness()));
        return commercialProperty;
    }
}
