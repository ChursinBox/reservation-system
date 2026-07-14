package com.some.project.dataJpa.reservation_system;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class ReservationController {



    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/{id}")
    public Reservation getReservationById(@PathVariable("id") Long id) {

        log.info("Called getReservationById: id= " + id);
        return reservationService.getReservationById(id);
    }

    @GetMapping()
    public List<Reservation> getAllReservations() {

        log.info("Called getAllReservations");
        return reservationService.findAllReservations();
    }

    @PostMapping
    public Reservation createReservation(@RequestBody Reservation reservationToCreate) {
        log.info("Called createReservation");
        return reservationService.createReservation(reservationToCreate);
    }
}
