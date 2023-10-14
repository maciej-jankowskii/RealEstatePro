package com.realestate.controller;

import com.realestate.dto.ApartmentPropertyDto;
import com.realestate.service.ApartmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/apartment")
public class ApartmentController {
    private final ApartmentService apartmentService;

    public ApartmentController(ApartmentService apartmentService) {
        this.apartmentService = apartmentService;
    }

    @PostMapping
    public ResponseEntity<ApartmentPropertyDto> saveApartment(@Valid @RequestBody ApartmentPropertyDto apartmentDto){
        ApartmentPropertyDto saved = apartmentService.saveApartment(apartmentDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(uri).body(saved);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApartmentPropertyDto> getApartmentById(@PathVariable Long id){
        return apartmentService.getCompanyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ApartmentPropertyDto>> getAllApartments(){
        List<ApartmentPropertyDto> allApartments = apartmentService.getAllApartments();
        if (allApartments.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allApartments);
    }


}
