package com.realestate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.realestate.dto.OfferDto;
import com.realestate.service.OfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/offer")
public class OfferController {

    private final OfferService offerService;
    private final ObjectMapper objectMapper;

    public OfferController(OfferService offerService, ObjectMapper objectMapper) {
        this.offerService = offerService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<OfferDto> saveOffer(@RequestBody OfferDto offerDto) {
        OfferDto saved = offerService.saveOffer(offerDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(uri).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferDto> getOfferById(@PathVariable Long id) {
        return offerService.getOfferById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<List<OfferDto>> getOfferByClientId(@PathVariable Long id) {
        List<OfferDto> offersByClient = offerService.getOffersByClient(id);
        if (offersByClient.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(offersByClient);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<OfferDto>> getAllOffers() {
        List<OfferDto> allOffers = offerService.getAllOffers();
        if (allOffers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allOffers);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateOffer(@PathVariable Long id, @RequestBody JsonMergePatch patch) {
        try {
            OfferDto offerDto = offerService.getOfferById(id).orElseThrow();
            OfferDto patchedOffer = applyPatch(offerDto, patch);
            offerService.updateOffer(patchedOffer);
        } catch (JsonPatchException | JsonProcessingException ex) {
            return ResponseEntity.internalServerError().build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOffer(@PathVariable Long id) {
        offerService.deleteOffer(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/sellOffer/{id}")
    public ResponseEntity<?> sellOfferById(@PathVariable Long id) {
        offerService.markOfferAsSold(id);
        return ResponseEntity.ok("Offer marked as sold ");
    }

    @GetMapping("/available")
    public ResponseEntity<List<OfferDto>> getAvailableOffers() {
        List<OfferDto> availableOffers = offerService.findAvailableOffers();
        if (availableOffers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(availableOffers);
    }

    @GetMapping("/sold")
    public ResponseEntity<List<OfferDto>> getSoldOffers() {
        List<OfferDto> soldOffers = offerService.findSoldOffers();
        if (soldOffers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(soldOffers);
    }

    private OfferDto applyPatch(OfferDto dto, JsonMergePatch patch) throws JsonPatchException, JsonProcessingException {
        JsonNode offerNode = objectMapper.valueToTree(dto);
        JsonNode offerPatched = patch.apply(offerNode);
        return objectMapper.treeToValue(offerPatched, OfferDto.class);
    }

}
