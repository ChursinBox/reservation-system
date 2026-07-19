package com.some.project.dataJpa.reservation_system;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reservation")
@Slf4j

public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable("id") Long id) {
        log.info("Called getReservationById: id= {}", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(reservationService.getReservationById(id));

    }

    @GetMapping()
    public ResponseEntity<List<Reservation>> getAllReservations() {
        log.info("Called getAllReservations");
        return ResponseEntity.ok(reservationService.findAllReservations());
    }

    @PostMapping()
    public ResponseEntity<Reservation> createReservation(@RequestBody @Valid Reservation reservationToCreate) {
        log.info("Called createReservation");
        return ResponseEntity.status(201)
                .header("test-header", "123")
                .body(reservationService.createReservation(reservationToCreate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(
            @PathVariable("id") Long id,
            @RequestBody @Valid Reservation reservationToUpdate) {
        log.info("Called updateReservation id={}, reservationToUpdate={}",
                id, reservationToUpdate);

        var updated = reservationService.updateReservation(id, reservationToUpdate);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Reservation> deleteReservation(@PathVariable("id") Long id) {
        log.info("Called deleteReservation: id={}", id);
        reservationService.cancelReservation(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Reservation> approveReservation(@PathVariable("id") Long id) {
        log.info("Called approve reservation");
        var reservation = reservationService.approveReservation(id);
        return ResponseEntity.ok(reservation);
    }
}
