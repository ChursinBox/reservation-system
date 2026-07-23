package com.some.project.dataJpa.reservation_system.reservations.avaliability;

import com.some.project.dataJpa.reservation_system.reservations.Reservation;
import com.some.project.dataJpa.reservation_system.reservations.ReservationRepository;
import com.some.project.dataJpa.reservation_system.reservations.ReservationStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class ReservationAvailabilityService {

    private final ReservationRepository repository;

    public ReservationAvailabilityService(ReservationRepository repository) {
        this.repository = repository;
    }

    public boolean isReservationAvailable(
            Long roomId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (!endDate.isAfter(startDate)) {
            throw new IllegalArgumentException("Start date must be 1 day earlier then end date");
        }
        List<Long> conflictIds = repository.findConflictReservationsId(
                roomId,
                startDate,
                endDate,
                ReservationStatus.APPROVED
        );
        if (conflictIds.isEmpty()) {
            return true;
        }
        log.info("Conflict with ids = {}", conflictIds);
        return false;
    }
}
