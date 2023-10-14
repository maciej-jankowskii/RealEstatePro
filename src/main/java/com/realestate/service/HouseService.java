package com.realestate.service;

import com.realestate.dto.HousePropertyDto;
import com.realestate.mapper.HouseMapper;
import com.realestate.model.Property.House;
import com.realestate.repository.HouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HouseService {

    private final HouseRepository houseRepository;
    private final HouseMapper houseMapper;

    public HouseService(HouseRepository houseRepository, HouseMapper houseMapper) {
        this.houseRepository = houseRepository;
        this.houseMapper = houseMapper;
    }

    @Transactional
    public HousePropertyDto saveHouse(HousePropertyDto dto){
        House house = houseMapper.map(dto);
        House saved = houseRepository.save(house);
        return houseMapper.map(saved);
    }

    public Optional<HousePropertyDto> getHouseById(Long id){
        return houseRepository.findById(id).map(houseMapper::map);
    }

    public List<HousePropertyDto> getAllHouses(){
        List<House> allHouses = (List<House>) houseRepository.findAll();
        List<HousePropertyDto> dtos = allHouses.stream().map(houseMapper::map).collect(Collectors.toList());
        return dtos;
    }

    @Transactional
    public void updateHouse(HousePropertyDto dto){
        House house = houseMapper.map(dto);
        houseRepository.save(house);
    }

    public void deleteHouse(Long id){
        houseRepository.deleteById(id);
    }
}
