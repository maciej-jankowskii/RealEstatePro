package com.realestate.mapper;

import com.realestate.dto.ApartmentPropertyDto;
import com.realestate.enums.BuildingType;
import com.realestate.enums.Standard;
import com.realestate.model.Property.Apartment;
import org.springframework.stereotype.Service;

@Service
public class ApartmentMapper {
    public ApartmentPropertyDto map(Apartment apartment){
        ApartmentPropertyDto dto = new ApartmentPropertyDto();
        dto.setId(apartment.getId());
        dto.setAddress(apartment.getAddress());
        dto.setPrice(apartment.getPrice());
        dto.setDescription(apartment.getDescription());
        dto.setArea(apartment.getArea());
        dto.setRooms(apartment.getRooms());
        dto.setBathrooms(apartment.getBathrooms());
        dto.setDuplexApartment(apartment.getDuplexApartment());
        dto.setBuildingType(String.valueOf(apartment.getBuildingType()));
        dto.setFloor(apartment.getFloor());
        dto.setElevator(apartment.getElevator());
        dto.setBalcony(apartment.getBalcony());
        dto.setGarage(apartment.getGarage());
        dto.setYearOfConstruction(apartment.getYearOfConstruction());
        dto.setStandard(String.valueOf(apartment.getStandard()));
        return dto;
    }

    public Apartment map(ApartmentPropertyDto dto){
        Apartment apartment = new Apartment();
        apartment.setId(dto.getId());
        apartment.setAddress(dto.getAddress());
        apartment.setPrice(dto.getPrice());
        apartment.setDescription(dto.getDescription());
        apartment.setArea(dto.getArea());
        apartment.setRooms(dto.getRooms());
        apartment.setBathrooms(dto.getBathrooms());
        apartment.setDuplexApartment(dto.getDuplexApartment());
        apartment.setBuildingType(BuildingType.valueOf(dto.getBuildingType()));
        apartment.setFloor(dto.getFloor());
        apartment.setElevator(dto.getElevator());
        apartment.setBalcony(dto.getBalcony());
        apartment.setGarage(dto.getGarage());
        apartment.setYearOfConstruction(dto.getYearOfConstruction());
        apartment.setStandard(Standard.valueOf(dto.getStandard()));
        return apartment;

    }
}
