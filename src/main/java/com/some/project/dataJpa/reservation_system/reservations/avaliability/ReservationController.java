package com.some.project.dataJpa.reservation_system.reservations.avaliability;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservation/availability")
@Slf4j
public class ReservationController {
    private final ReservationAvailabilityService service;

    public ReservationController(ReservationAvailabilityService service) {
        this.service = service;
    }

    @PostMapping("/check")
    public ResponseEntity<CheckAvailabilityResponse> checkAvailability(
            @Valid CheckAvailabilityRequest request
    ) {
        log.info("Called method checkAvailability: request={}", request);
        var reservationAvailable = service.isReservationAvailable(
                request.roomId(), request.startDate(), request.endDate()
        );
        var message = reservationAvailable ? "Room svobodno!!!" : "Room zaniato!!!";
        var status = reservationAvailable ? AvailabilityStatus.AVAILABLE : AvailabilityStatus.RESERVED;

        return ResponseEntity.status(200).body(new CheckAvailabilityResponse(message, status));
    }
}
