package com.realestate.controller;

import com.realestate.dto.ApartmentPropertyDto;
import com.realestate.exceptions.CannotDeleteResourceException;
import com.realestate.service.ApartmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/apartments")
@CrossOrigin("*")
public class ApartmentController {
    private final ApartmentService apartmentService;

    public ApartmentController(ApartmentService apartmentService) {
        this.apartmentService = apartmentService;
    }

    @PostMapping
    public ResponseEntity<ApartmentPropertyDto> saveApartment(@Valid @RequestBody ApartmentPropertyDto apartmentDto) {
        ApartmentPropertyDto saved = apartmentService.saveApartment(apartmentDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(uri).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApartmentPropertyDto> getApartmentById(@PathVariable Long id) {
        ApartmentPropertyDto dto = apartmentService.getApartmentById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("")
    public ResponseEntity<List<ApartmentPropertyDto>> getAllApartments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        List<ApartmentPropertyDto> allApartments = apartmentService.getAllApartments(page, size);
        if (allApartments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allApartments);
    }

//    @GetMapping("/filtered")
//    public ResponseEntity<List<ApartmentPropertyDto>> filterApartments(
//            @RequestParam(required = false) String address,
//            @RequestParam(required = false) BigDecimal maxPrice,
//            @RequestParam(required = false) Double minArea,
//            @RequestParam(required = false) Double maxArea,
//            @RequestParam(required = false) Integer rooms,
//            @RequestParam(required = false) Integer bathrooms,
//            @RequestParam(required = false) Boolean duplexApartment,
//            @RequestParam(required = false) String buildingType,
//            @RequestParam(required = false) Integer maxFloor,
//            @RequestParam(required = false) Boolean elevator,
//            @RequestParam(required = false) Boolean balcony,
//            @RequestParam(required = false) Boolean garage,
//            @RequestParam(required = false) Integer minYearOfConstruction,
//            @RequestParam(required = false) String standard) {
//        List<ApartmentPropertyDto> filteredApartments = apartmentService.filterApartments(
//                address, maxPrice, minArea,
//                maxArea, rooms, bathrooms, duplexApartment,
//                buildingType, maxFloor, elevator,
//                balcony, garage, minYearOfConstruction, standard);
//        return ResponseEntity.ok(filteredApartments);
//
//
//    }

    @PutMapping("/update-apartment/{id}")
    public ResponseEntity<?> updateApartment(@PathVariable Long id, @RequestBody ApartmentPropertyDto updateDto) {
        apartmentService.updateApartment(id, updateDto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-apartment/{id}")
    ResponseEntity<?> deleteApartment(@PathVariable Long id) {
        apartmentService.deleteApartment(id);
        return ResponseEntity.noContent().build();

    }
}
