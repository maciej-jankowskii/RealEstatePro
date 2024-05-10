package com.realestate.controller;

import com.realestate.dto.LandPropertyDto;
import com.realestate.service.LandService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/lands")
@CrossOrigin("*")
public class LandController {

    private final LandService landService;

    public LandController(LandService landService) {
        this.landService = landService;
    }

    @PostMapping
    public ResponseEntity<LandPropertyDto> saveLandProperty(@Valid @RequestBody LandPropertyDto dto) {
        LandPropertyDto saved = landService.saveLand(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(uri).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LandPropertyDto> getLandById(@PathVariable Long id) {
        LandPropertyDto land = landService.getLandById(id);
        return ResponseEntity.ok(land);
    }

    @GetMapping("")
    public ResponseEntity<List<LandPropertyDto>> getAllLands(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        List<LandPropertyDto> allLands = landService.getAllLands(page, size);
        if (allLands.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allLands);
    }

//    @GetMapping("/filtered")
//    public ResponseEntity<List<LandPropertyDto>> filterLands(
//            @RequestParam(required = false) String address,
//            @RequestParam(required = false) BigDecimal minPrice,
//            @RequestParam(required = false) BigDecimal maxPrice,
//            @RequestParam(required = false) String typeOfLand,
//            @RequestParam(required = false) Double minArea,
//            @RequestParam(required = false) Double maxArea,
//            @RequestParam(required = false) Boolean buildingPermit
//    ) {
//        List<LandPropertyDto> filteredLands = landService.filterLands(
//                address, minPrice, maxPrice,
//                typeOfLand, minArea, maxArea, buildingPermit);
//        return ResponseEntity.ok(filteredLands);
//
//    }

    @PutMapping("/update-land/{id}")
    public ResponseEntity<?> updateLand(@PathVariable Long id, @RequestBody LandPropertyDto updateDto) {
        landService.updateLand(id, updateDto);
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/delete-land/{id}")
    public ResponseEntity<?> deleteLand(@PathVariable Long id) {
        landService.deleteLand(id);
        return ResponseEntity.noContent().build();
    }
}
