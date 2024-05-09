package com.realestate.service;

import com.realestate.dto.CommercialPropertyDto;
import com.realestate.enums.BuildingType;
import com.realestate.enums.TypeOfBusiness;
import com.realestate.exceptions.CannotDeleteResourceException;
import com.realestate.exceptions.ResourceNotFoundException;
import com.realestate.mapper.CommercialMapper;
import com.realestate.model.Property.CommercialProperty;
import com.realestate.repository.CommercialRepository;
import com.realestate.repository.OffersRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommercialService {

    private final CommercialRepository commercialRepository;
    private final CommercialMapper commercialMapper;
    private final OffersRepository offersRepository;

    public CommercialService(CommercialRepository commercialRepository, CommercialMapper commercialMapper, OffersRepository offersRepository) {
        this.commercialRepository = commercialRepository;
        this.commercialMapper = commercialMapper;
        this.offersRepository = offersRepository;
    }

    @Transactional
    public CommercialPropertyDto saveCommercialProperty(CommercialPropertyDto dto) {
        CommercialProperty commercialProperty = commercialMapper.map(dto);
        CommercialProperty saved = commercialRepository.save(commercialProperty);
        return commercialMapper.map(saved);
    }

    public CommercialPropertyDto getCommercialPropertyById(Long id) {
        return commercialRepository.findById(id).map(commercialMapper::map).orElseThrow(() -> new ResourceNotFoundException("Commercial Property not found"));
    }

    public List<CommercialPropertyDto> getAllCommercialProperty(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<CommercialProperty> commercialPage = commercialRepository.findAll(pageable);
        List<CommercialPropertyDto> commercialDtos = commercialPage.getContent().stream()
                .map(commercialMapper::map)
                .collect(Collectors.toList());
        return commercialDtos;

    }

//    public List<CommercialPropertyDto> filterCommercialProperties(
//            String address,
//            BigDecimal maxPrice,
//            Double minArea,
//            Double maxArea,
//            Integer rooms,
//            Integer bathrooms,
//            String buildingType,
//            Integer maxFloor,
//            String typeOfBusiness
//    ) {
//        List<CommercialProperty> allCommercialProperties = (List<CommercialProperty>) commercialRepository.findAll();
//        return allCommercialProperties.stream()
//                .filter(commercial -> (address == null || commercial.getAddress().contains(address))
//                        && (maxPrice == null || commercial.getPrice().compareTo(maxPrice) <= 0)
//                        && (minArea == null || commercial.getArea() >= minArea)
//                        && (maxArea == null || commercial.getArea() <= maxArea)
//                        && ((rooms == null) || commercial.getRooms().equals(rooms))
//                        && (bathrooms == null || commercial.getBathrooms().equals(bathrooms))
//                        && (buildingType == null || commercial.getBuildingType().name().equals(buildingType))
//                        && (maxFloor == null || commercial.getFloor() <= maxFloor)
//                        && (typeOfBusiness == null || commercial.getTypeOfBusiness().name().equals(typeOfBusiness)))
//                .map(commercialMapper::map)
//                .collect(Collectors.toList());
//    }



    @Transactional
    public void updateCommercialProperty(Long id, CommercialPropertyDto updateDto) {
        CommercialProperty commercialProperty = commercialRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Commercial Property not found"));
        updateCommercialProperty(updateDto, commercialProperty);

        commercialRepository.save(commercialProperty);
    }


    public void deleteCommercialProperty(Long id) {
        CommercialProperty commercialProperty = commercialRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Commercial Property not found"));

        if (offersRepository.existsByPropertyId(commercialProperty.getId())){
            throw new CannotDeleteResourceException("Cannot remove commercial property assigned to an offer");
        }

        commercialRepository.deleteById(id);
    }


    private static void updateCommercialProperty(CommercialPropertyDto updateDto, CommercialProperty commercialProperty) {
        commercialProperty.setAddress(updateDto.getAddress());
        commercialProperty.setPrice(updateDto.getPrice());
        commercialProperty.setDescription(updateDto.getDescription());
        commercialProperty.setBuildingType(BuildingType.valueOf(updateDto.getBuildingType()));
        commercialProperty.setTypeOfBusiness(TypeOfBusiness.valueOf(updateDto.getTypeOfBusiness()));
        commercialProperty.setFloor(updateDto.getFloor());
        commercialProperty.setRooms(updateDto.getRooms());
        commercialProperty.setBathrooms(updateDto.getBathrooms());
        commercialProperty.setArea(updateDto.getArea());
    }
}
