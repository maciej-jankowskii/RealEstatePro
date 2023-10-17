package com.realestate.service;

import com.realestate.dto.CommercialPropertyDto;
import com.realestate.mapper.CommercialMapper;
import com.realestate.model.Property.CommercialProperty;
import com.realestate.repository.CommercialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommercialService {

    private final CommercialRepository commercialRepository;
    private final CommercialMapper commercialMapper;

    public CommercialService(CommercialRepository commercialRepository, CommercialMapper commercialMapper) {
        this.commercialRepository = commercialRepository;
        this.commercialMapper = commercialMapper;
    }
    @Transactional
    public CommercialPropertyDto saveCommercialProperty(CommercialPropertyDto dto){
        CommercialProperty commercialProperty = commercialMapper.map(dto);
        CommercialProperty saved = commercialRepository.save(commercialProperty);
        return commercialMapper.map(saved);
    }

    public Optional<CommercialPropertyDto> getCommercialPropertyById(Long id){
        return commercialRepository.findById(id).map(commercialMapper::map);
    }

    public List<CommercialPropertyDto> getAllCommercialProperty(){
        List<CommercialProperty> allCommercial = (List<CommercialProperty>) commercialRepository.findAll();
        List<CommercialPropertyDto> dtos = allCommercial.stream().map(commercialMapper::map).collect(Collectors.toList());
        return dtos;
    }

    public List<CommercialPropertyDto> filterCommercialProperties(
            String address,
            BigDecimal maxPrice,
            Double minArea,
            Double maxArea,
            Integer rooms,
            Integer bathrooms,
            String buildingType,
            Integer maxFloor,
            String typeOfBusiness
    ){
        List<CommercialProperty> allCommercialProperties = (List<CommercialProperty>) commercialRepository.findAll();
        return allCommercialProperties.stream()
                .filter(commercial -> (address == null || commercial.getAddress().contains(address))
                && (maxPrice == null || commercial.getPrice().compareTo(maxPrice) <= 0)
                && (minArea == null || commercial.getArea() >= minArea)
                && (maxArea == null || commercial.getArea() <= maxArea)
                && ((rooms == null) || commercial.getRooms().equals(rooms))
                && (bathrooms == null || commercial.getBathrooms().equals(bathrooms))
                && (buildingType == null || commercial.getBuildingType().name().equals(buildingType))
                && (maxFloor == null || commercial.getFloor() <= maxFloor)
                && (typeOfBusiness == null || commercial.getTypeOfBusiness().name().equals(typeOfBusiness)))
                .map(commercialMapper::map)
                .collect(Collectors.toList());
    }
    @Transactional
    public void updateCommercialProperty(CommercialPropertyDto dto){
        CommercialProperty commercialProperty = commercialMapper.map(dto);
        commercialRepository.save(commercialProperty);
    }

    public void deleteCommercialProperty(Long id){
        commercialRepository.deleteById(id);
    }
}
