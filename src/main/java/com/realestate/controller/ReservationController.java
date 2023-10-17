package com.realestate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.realestate.dto.ReservationDto;
import com.realestate.mapper.ReservationMapper;
import com.realestate.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;
    private final ObjectMapper objectMapper;

    public ReservationController(ReservationService reservationService, ReservationMapper reservationMapper, ObjectMapper objectMapper) {
        this.reservationService = reservationService;
        this.reservationMapper = reservationMapper;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<ReservationDto> saveReservation(@RequestBody ReservationDto reservationDto) {
        ReservationDto saved = reservationService.saveReservation(reservationDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(uri).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/offer/{id}")
    public ResponseEntity<ReservationDto> getReservationByOfferId(@PathVariable Long id) {
        return reservationService.getReservationByOfferId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ReservationDto>> getAllReservations() {
        List<ReservationDto> allReservation = reservationService.getAllReservation();
        if (allReservation.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allReservation);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateReservation(@PathVariable Long id, @RequestBody JsonMergePatch patch) {
        try {
            ReservationDto reservationDto = reservationService.getReservationById(id).orElseThrow();
            ReservationDto patchedReservation = applyPatch(reservationDto, patch);
            reservationService.updateReservation(patchedReservation);
        } catch (JsonPatchException | JsonProcessingException ex) {
            return ResponseEntity.internalServerError().build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    private ReservationDto applyPatch(ReservationDto dto, JsonMergePatch patch) throws JsonPatchException, JsonProcessingException {
        JsonNode reservationNode = objectMapper.valueToTree(dto);
        JsonNode patchedReservation = patch.apply(reservationNode);
        return objectMapper.treeToValue(patchedReservation, ReservationDto.class);
    }

}
