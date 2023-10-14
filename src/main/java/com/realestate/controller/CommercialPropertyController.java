package com.realestate.controller;

import com.realestate.dto.CommercialPropertyDto;
import com.realestate.service.CommercialService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/commercial")
public class CommercialPropertyController {
    private final CommercialService commercialService;

    public CommercialPropertyController(CommercialService commercialService) {
        this.commercialService = commercialService;
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

}
