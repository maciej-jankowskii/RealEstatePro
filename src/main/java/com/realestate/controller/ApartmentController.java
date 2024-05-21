package com.realestate.controller;

import com.realestate.dto.ApartmentPropertyDto;
import com.realestate.service.ApartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/apartments")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ApartmentController {

    private final ApartmentService apartmentService;


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

    @PutMapping("/update-apartment/{id}")
    public ResponseEntity<?> updateApartment(@PathVariable Long id, @RequestBody @Valid ApartmentPropertyDto updateDto) {
        apartmentService.updateApartment(id, updateDto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-apartment/{id}")
    ResponseEntity<?> deleteApartment(@PathVariable Long id) {
        apartmentService.deleteApartment(id);
        return ResponseEntity.noContent().build();

    }
}
