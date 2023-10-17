package com.realestate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.realestate.dto.HousePropertyDto;
import com.realestate.service.HouseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/house")
public class HouseController {

    private final HouseService houseService;
    private final ObjectMapper objectMapper;

    public HouseController(HouseService houseService, ObjectMapper objectMapper) {
        this.houseService = houseService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<HousePropertyDto> saveHouse(@Valid @RequestBody HousePropertyDto dto){
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

    @GetMapping("/getAll")
    public ResponseEntity<List<HousePropertyDto>> getAllHouses(){
        List<HousePropertyDto> allHouses = houseService.getAllHouses();
        if (allHouses.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allHouses);
    }

    @GetMapping("/filtered")
    public ResponseEntity<List<HousePropertyDto>> filterHouses(
            @RequestParam(required = false) String address,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Double minLandArea,
            @RequestParam(required = false) Double maxLandArea,
            @RequestParam(required = false) Double minHouseArea,
            @RequestParam(required = false) Double maxHouseArea,
            @RequestParam(required = false) Integer rooms,
            @RequestParam(required = false) Integer bathrooms,
            @RequestParam(required = false) Boolean balcony,
            @RequestParam(required = false) Boolean garage,
            @RequestParam(required = false) Boolean twoStoryHouse,
            @RequestParam(required = false) String buildingType,
            @RequestParam(required = false) Integer minYearOfConstruction,
            @RequestParam(required = false) String standard
    ){
        List<HousePropertyDto> filteredHouses = houseService.filterHouses(
                address, minPrice, maxPrice,
                minLandArea, maxLandArea, minHouseArea,
                maxHouseArea, rooms, bathrooms, balcony,
                garage, twoStoryHouse, buildingType,
                minYearOfConstruction, standard);
        return ResponseEntity.ok(filteredHouses);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateHouse(@PathVariable Long id, @RequestBody JsonMergePatch patch){
        try {
            HousePropertyDto houseDto = houseService.getHouseById(id).orElseThrow();
            HousePropertyDto patchedHouse = applyPatch(houseDto, patch);
            houseService.updateHouse(patchedHouse);
        } catch (JsonPatchException | JsonProcessingException ex){
            return ResponseEntity.internalServerError().build();
        }catch (NoSuchElementException ex){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHouse(@PathVariable Long id){
        houseService.deleteHouse(id);
        return ResponseEntity.noContent().build();
    }

    private HousePropertyDto applyPatch(HousePropertyDto dto, JsonMergePatch patch) throws JsonPatchException, JsonProcessingException {
        JsonNode houseNode = objectMapper.valueToTree(dto);
        JsonNode housePatchedNode = patch.apply(houseNode);
        return objectMapper.treeToValue(housePatchedNode, HousePropertyDto.class);
    }
}
