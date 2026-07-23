package com.some.project.dataJpa.reservation_system.reservations.avaliability;

public record CheckAvailabilityResponse(
        String message,
        AvailabilityStatus status
) {
}
