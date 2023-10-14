package com.realestate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.realestate.dto.LandPropertyDto;
import com.realestate.service.LandService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/land")
public class LandController {

    private final LandService landService;
    private final ObjectMapper objectMapper;

    public LandController(LandService landService, ObjectMapper objectMapper) {
        this.landService = landService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<LandPropertyDto> saveLandProperty(@Valid @RequestBody LandPropertyDto dto){
        LandPropertyDto saved = landService.saveLand(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(uri).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LandPropertyDto> getLandById(@PathVariable Long id){
        return landService.getLandById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<LandPropertyDto>> getAllLands(){
        List<LandPropertyDto> allLands = landService.getAllLands();
        if (allLands.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allLands);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateLand(@PathVariable Long id, @RequestBody JsonMergePatch patch){
        try {
            LandPropertyDto landDto = landService.getLandById(id).orElseThrow();
            LandPropertyDto patchedLand = applyPatch(landDto, patch);
            landService.updateLand(patchedLand);
        }catch (JsonProcessingException | JsonPatchException ex){
            return ResponseEntity.internalServerError().build();
        }catch (NoSuchElementException ex){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();

    }

    private LandPropertyDto applyPatch(LandPropertyDto dto, JsonMergePatch patch) throws JsonPatchException, JsonProcessingException {
        JsonNode landNode = objectMapper.valueToTree(dto);
        JsonNode patchedLand = patch.apply(landNode);
        return objectMapper.treeToValue(patchedLand, LandPropertyDto.class);
    }
}
