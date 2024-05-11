package com.realestate.controller;
import com.realestate.dto.OfferDto;
import com.realestate.service.OfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/offers")
@CrossOrigin("*")
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
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
        OfferDto offer = offerService.getOfferById(id);
        return ResponseEntity.ok(offer);
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<List<OfferDto>> getOfferByClientId(@PathVariable Long id) {
        List<OfferDto> offersByClient = offerService.getOffersByClient(id);
        if (offersByClient.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(offersByClient);
    }

    @GetMapping("")
    public ResponseEntity<List<OfferDto>> getAllOffers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        List<OfferDto> allOffers = offerService.getAllOffers(page, size);
        if (allOffers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allOffers);
    }

    @PutMapping("/update-offer/{id}")
    public ResponseEntity<?> updateOffer(@PathVariable Long id, @RequestBody OfferDto updateDto) {
        offerService.updateOffer(id, updateDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-offer/{id}")
    public ResponseEntity<?> deleteOffer(@PathVariable Long id) {
        offerService.deleteOffer(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/sold/{id}")
    public ResponseEntity<?> sellOfferById(@PathVariable Long id) {
        offerService.markOfferAsSold(id);
        return ResponseEntity.ok("Offer marked as sold ");
    }

}
