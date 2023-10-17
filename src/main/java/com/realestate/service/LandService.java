package com.realestate.service;

import com.realestate.dto.LandPropertyDto;
import com.realestate.mapper.LandMapper;
import com.realestate.model.Property.Land;
import com.realestate.repository.LandRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LandService {

    private final LandRepository landRepository;
    private final LandMapper landMapper;

    public LandService(LandRepository landRepository, LandMapper landMapper) {
        this.landRepository = landRepository;
        this.landMapper = landMapper;
    }

    @Transactional
    public LandPropertyDto saveLand(LandPropertyDto dto) {
        Land land = landMapper.map(dto);
        Land saved = landRepository.save(land);
        return landMapper.map(saved);
    }

    public Optional<LandPropertyDto> getLandById(Long id) {
        return landRepository.findById(id).map(landMapper::map);
    }

    public List<LandPropertyDto> getAllLands() {
        List<Land> allLands = (List<Land>) landRepository.findAll();
        List<LandPropertyDto> dtos = allLands.stream().map(landMapper::map).collect(Collectors.toList());
        return dtos;
    }

    public List<LandPropertyDto> filterLands(
            String address,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String typeOfLand,
            Double minArea,
            Double maxArea,
            Boolean buildingPermit
    ) {
        List<Land> allLands = (List<Land>) landRepository.findAll();
        return allLands.stream().filter(land -> (address == null || land.getAddress().contains(address))
                        && (minPrice == null || land.getPrice().compareTo(minPrice) >= 0)
                        && (maxPrice == null || land.getPrice().compareTo(maxPrice) <= 0)
                        && (typeOfLand == null || land.getTypeOfLand().name().equals(typeOfLand))
                        && (minArea == null || land.getArea() >= minArea)
                        && (maxArea == null || land.getArea() <= maxArea)
                        && (buildingPermit == null || land.getBuildingPermit().equals(buildingPermit)))
                .map(landMapper::map)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateLand(LandPropertyDto dto) {
        Land land = landMapper.map(dto);
        landRepository.save(land);
    }

    public void deleteLand(Long id) {
        landRepository.deleteById(id);
    }
}
