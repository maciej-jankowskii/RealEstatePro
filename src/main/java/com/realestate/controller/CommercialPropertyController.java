package com.realestate.controller;

import com.realestate.dto.CommercialPropertyDto;
import com.realestate.service.CommercialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/commercials")
@CrossOrigin("*")
@RequiredArgsConstructor
public class CommercialPropertyController {
    private final CommercialService commercialService;


    @PostMapping
    public ResponseEntity<CommercialPropertyDto> saveCommercialProperty(@Valid @RequestBody CommercialPropertyDto dto) {
        CommercialPropertyDto saved = commercialService.saveCommercialProperty(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(uri).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommercialPropertyDto> getCommercialPropertyById(@PathVariable Long id) {
        CommercialPropertyDto commercial = commercialService.getCommercialPropertyById(id);
        return ResponseEntity.ok(commercial);
    }

    @GetMapping("")
    public ResponseEntity<List<CommercialPropertyDto>> getAllCommercial(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        List<CommercialPropertyDto> allCommercial = commercialService.getAllCommercialProperty(page, size);
        if (allCommercial.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allCommercial);
    }


    @PutMapping("/update-commercial/{id}")
    public ResponseEntity<?> updateCommercialProperty(@PathVariable Long id, @RequestBody @Valid CommercialPropertyDto updateDto) {
        commercialService.updateCommercialProperty(id, updateDto);
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/delete-commercial/{id}")
    public ResponseEntity<?> deleteCommercialProperty(@PathVariable Long id) {
        commercialService.deleteCommercialProperty(id);
        return ResponseEntity.noContent().build();
    }

}
