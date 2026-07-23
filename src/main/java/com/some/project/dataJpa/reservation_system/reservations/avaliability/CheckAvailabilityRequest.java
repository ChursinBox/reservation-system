package com.some.project.dataJpa.reservation_system.reservations.avaliability;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CheckAvailabilityRequest(
        @NotNull
        Long roomId,

        @NotNull
        LocalDate startDate,

        @NotNull
        LocalDate endDate
) {
}
