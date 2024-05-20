package com.realestate.controller;

import com.realestate.dto.HousePropertyDto;
import com.realestate.service.HouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/houses")
@CrossOrigin("*")
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;


    @PostMapping
    public ResponseEntity<HousePropertyDto> saveHouse(@Valid @RequestBody HousePropertyDto dto) {
        HousePropertyDto saved = houseService.saveHouse(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(uri).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HousePropertyDto> getHouseById(@PathVariable Long id) {
        HousePropertyDto houseById = houseService.getHouseById(id);
        return ResponseEntity.ok(houseById);
    }

    @GetMapping("")
    public ResponseEntity<List<HousePropertyDto>> getAllHouses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        List<HousePropertyDto> allHouses = houseService.getAllHouses(page, size);
        if (allHouses.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allHouses);
    }


    @PutMapping("/update-house/{id}")
    public ResponseEntity<?> updateHouse(@PathVariable Long id, @RequestBody @Valid HousePropertyDto updateDto) {
        houseService.updateHouse(id, updateDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-house/{id}")
    public ResponseEntity<?> deleteHouse(@PathVariable Long id) {
        houseService.deleteHouse(id);
        return ResponseEntity.noContent().build();
    }

}
