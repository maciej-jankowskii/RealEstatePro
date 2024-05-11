package com.realestate.controller;

import com.realestate.dto.ReservationDto;
import com.realestate.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin("*")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
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
        ReservationDto reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping("/offer/{id}")
    public ResponseEntity<ReservationDto> getReservationByOfferId(@PathVariable Long id) {
        ReservationDto reservation = reservationService.getReservationByOfferId(id);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping("")
    public ResponseEntity<List<ReservationDto>> getAllReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        List<ReservationDto> allReservation = reservationService.getAllReservation(page, size);
        if (allReservation.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allReservation);
    }

    @PutMapping("/update-reservation/{id}")
    public ResponseEntity<?> updateReservation(@PathVariable Long id, @RequestBody ReservationDto updateDto) {
        reservationService.updateReservation(id, updateDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-reservation/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

}
