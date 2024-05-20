package com.realestate.service;

import com.realestate.dto.LandPropertyDto;
import com.realestate.enums.TypeOfLand;
import com.realestate.exceptions.CannotDeleteResourceException;
import com.realestate.exceptions.ResourceNotFoundException;
import com.realestate.mapper.LandMapper;
import com.realestate.model.Property.Land;
import com.realestate.repository.LandRepository;
import com.realestate.repository.OffersRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LandService {

    private final LandRepository landRepository;
    private final LandMapper landMapper;
    private final OffersRepository offersRepository;
    private final ValidationService validationService;


    @Transactional
    public LandPropertyDto saveLand(@Valid LandPropertyDto dto) {
        Land land = landMapper.map(dto);
        validationService.validateData(land);
        Land saved = landRepository.save(land);
        return landMapper.map(saved);
    }

    public LandPropertyDto getLandById(Long id) {
        return landRepository.findById(id).map(landMapper::map).orElseThrow(() -> new ResourceNotFoundException("Land not found"));
    }

    public List<LandPropertyDto> getAllLands(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Land> landPage = landRepository.findAll(pageable);
        return landPage.getContent().stream()
                .map(landMapper::map)
                .collect(Collectors.toList());

    }


    @Transactional
    public void updateLand(Long id, @Valid LandPropertyDto dto) {
        Land land = landRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Land not found"));
        validationService.validateData(land);
        updateLand(dto, land);
        landRepository.save(land);
    }

    public void deleteLand(Long id) {
        Land land = landRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Land not found"));

        if (offersRepository.existsByPropertyId(land.getId())){
            throw new CannotDeleteResourceException("Cannot delete land assigned to offer");
        }

        landRepository.deleteById(id);
    }


    private static void updateLand(LandPropertyDto dto, Land land) {
        land.setAddress(dto.getAddress());
        land.setPrice(dto.getPrice());
        land.setDescription(dto.getDescription());
        land.setTypeOfLand(TypeOfLand.valueOf(dto.getTypeOfLand()));
        land.setArea(dto.getArea());
        land.setBuildingPermit(dto.getBuildingPermit());
    }
}
