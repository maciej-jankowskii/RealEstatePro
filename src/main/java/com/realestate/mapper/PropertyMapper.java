package com.realestate.mapper;

import com.realestate.dto.PropertyDto;
import com.realestate.model.Property.Property;
import org.springframework.stereotype.Service;

@Service
public class PropertyMapper {

    public PropertyDto map(Property property){
        PropertyDto dto = new PropertyDto();
        dto.setId(property.getId());
        dto.setAddress(property.getAddress());
        dto.setPrice(property.getPrice());
        dto.setDescription(property.getDescription());
        return dto;
    }

    public Property map(PropertyDto propertyDto){
        Property property = new Property();
        property.setId(propertyDto.getId());
        property.setAddress(propertyDto.getAddress());
        property.setPrice(propertyDto.getPrice());
        property.setDescription(propertyDto.getDescription());
        return property;

    }
}
