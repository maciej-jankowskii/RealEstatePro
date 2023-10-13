package com.realestate.controller;

import com.realestate.dto.HousePropertyDto;
import com.realestate.service.HouseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/house")
public class HouseController {

    private final HouseService houseService;

    public HouseController(HouseService houseService) {
        this.houseService = houseService;
    }

    @PostMapping
    public ResponseEntity<HousePropertyDto> saveHouse(@RequestBody HousePropertyDto dto){
        HousePropertyDto saved = houseService.saveHouse(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(uri).body(saved);
    }
    @GetMapping("/{id}")
    public ResponseEntity<HousePropertyDto> getHouseById(@PathVariable Long id){
        return houseService.getHouseById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
