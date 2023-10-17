package com.realestate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.realestate.dto.ApartmentPropertyDto;
import com.realestate.service.ApartmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/apartment")
public class ApartmentController {
    private final ApartmentService apartmentService;
    private final ObjectMapper objectMapper;

    public ApartmentController(ApartmentService apartmentService, ObjectMapper objectMapper) {
        this.apartmentService = apartmentService;
        this.objectMapper = objectMapper;
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
        return apartmentService.getApartmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ApartmentPropertyDto>> getAllApartments() {
        List<ApartmentPropertyDto> allApartments = apartmentService.getAllApartments();
        if (allApartments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allApartments);
    }

    @GetMapping("/filtered")
    public ResponseEntity<List<ApartmentPropertyDto>> filterApartments(
            @RequestParam(required = false) String address,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Double minArea,
            @RequestParam(required = false) Double maxArea,
            @RequestParam(required = false) Integer rooms,
            @RequestParam(required = false) Integer bathrooms,
            @RequestParam(required = false) Boolean duplexApartment,
            @RequestParam(required = false) String buildingType,
            @RequestParam(required = false) Integer maxFloor,
            @RequestParam(required = false) Boolean elevator,
            @RequestParam(required = false) Boolean balcony,
            @RequestParam(required = false) Boolean garage,
            @RequestParam(required = false) Integer minYearOfConstruction,
            @RequestParam(required = false) String standard) {
        List<ApartmentPropertyDto> filteredApartments = apartmentService.filterApartments(
                address, maxPrice, minArea,
                maxArea, rooms, bathrooms, duplexApartment,
                buildingType, maxFloor, elevator,
                balcony, garage, minYearOfConstruction, standard);
        return ResponseEntity.ok(filteredApartments);


    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateApartment(@PathVariable Long id, @RequestBody JsonMergePatch patch) {
        try {
            ApartmentPropertyDto apartmentDto = apartmentService.getApartmentById(id).orElseThrow();
            ApartmentPropertyDto apartmentPatched = applyPatch(apartmentDto, patch);
            apartmentService.updateApartment(apartmentPatched);
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.internalServerError().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteApartment(@PathVariable Long id) {
        apartmentService.deleteApartment(id);
        return ResponseEntity.noContent().build();
    }

    private ApartmentPropertyDto applyPatch(ApartmentPropertyDto dto, JsonMergePatch patch) throws JsonPatchException, JsonProcessingException {
        JsonNode apartmentNode = objectMapper.valueToTree(dto);
        JsonNode apartmentPatchedNode = patch.apply(apartmentNode);
        return objectMapper.treeToValue(apartmentPatchedNode, ApartmentPropertyDto.class);
    }

}
