package com.realestate.service;

import com.realestate.dto.PropertyDto;
import com.realestate.mapper.PropertyMapper;
import com.realestate.model.Property.Property;
import com.realestate.model.user.UserEmployee;
import com.realestate.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;

    public List<PropertyDto> getAllProperties(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Property> properties = propertyRepository.findAll(pageable);
        return properties.getContent().stream()
                .map(propertyMapper::map)
                .collect(Collectors.toList());
    }

    public List<PropertyDto> searchProperties(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Property> properties = propertyRepository.searchByKeyword(keyword, pageable);
        return properties.getContent().stream()
                .map(propertyMapper::map)
                .collect(Collectors.toList());
    }



}
