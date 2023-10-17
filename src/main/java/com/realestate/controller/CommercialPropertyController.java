package com.realestate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.realestate.dto.CommercialPropertyDto;
import com.realestate.service.CommercialService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/commercial")
public class CommercialPropertyController {
    private final CommercialService commercialService;
    private final ObjectMapper objectMapper;

    public CommercialPropertyController(CommercialService commercialService, ObjectMapper objectMapper) {
        this.commercialService = commercialService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<CommercialPropertyDto> saveCommercialProperty(@Valid @RequestBody CommercialPropertyDto dto){
        CommercialPropertyDto saved = commercialService.saveCommercialProperty(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(uri).body(saved);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CommercialPropertyDto> getCommercialPropertyById(@PathVariable Long id){
        return commercialService.getCommercialPropertyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<CommercialPropertyDto>> getAllCommercial(){
        List<CommercialPropertyDto> allCommercial = commercialService.getAllCommercialProperty();
        if (allCommercial.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allCommercial);
    }

    @GetMapping("/filtered")
    public ResponseEntity<List<CommercialPropertyDto>> filterCommercialProperties(
            @RequestParam(required = false) String address,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Double minArea,
            @RequestParam(required = false) Double maxArea,
            @RequestParam(required = false) Integer rooms,
            @RequestParam(required = false) Integer bathrooms,
            @RequestParam(required = false) String buildingType,
            @RequestParam(required = false) Integer maxFloor,
            @RequestParam(required = false) String typeOfBusiness
    ) {
        List<CommercialPropertyDto> filteredCommercialProperties = commercialService.filterCommercialProperties(
                address, maxPrice, minArea,
                maxArea, rooms, bathrooms,
                buildingType, maxFloor, typeOfBusiness);
        return ResponseEntity.ok(filteredCommercialProperties);

    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCommercialProperty(@PathVariable Long id, @RequestBody JsonMergePatch patch){
        try {
            CommercialPropertyDto commercialPropertyDto = commercialService.getCommercialPropertyById(id).orElseThrow();
            CommercialPropertyDto commercialPatched = applyPatch(commercialPropertyDto, patch);
            commercialService.updateCommercialProperty(commercialPatched);
        } catch (JsonProcessingException | JsonPatchException e){
            return ResponseEntity.internalServerError().build();
        } catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCommercialProperty(@PathVariable Long id){
        commercialService.deleteCommercialProperty(id);
        return ResponseEntity.noContent().build();
    }

    private CommercialPropertyDto applyPatch(CommercialPropertyDto dto, JsonMergePatch patch) throws JsonPatchException, JsonProcessingException {
        JsonNode commercialNode = objectMapper.valueToTree(dto);
        JsonNode commercialPatchedNode = patch.apply(commercialNode);
        return objectMapper.treeToValue(commercialPatchedNode, CommercialPropertyDto.class);

    }

}
