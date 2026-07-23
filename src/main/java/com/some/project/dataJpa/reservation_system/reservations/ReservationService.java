package com.some.project.dataJpa.reservation_system.reservations;

import com.some.project.dataJpa.reservation_system.reservations.avaliability.ReservationAvailabilityService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ReservationService {

    private final ReservationRepository repository;
    private final ReservationAvailabilityService service;
    private final ReservationMapper mapper;

    public ReservationService(ReservationRepository repository, ReservationAvailabilityService service, ReservationMapper mapper) {
        this.repository = repository;
        this.service = service;
        this.mapper = mapper;
    }

    public Reservation getReservationById(Long id) {
        ReservationEntity reservationEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found reservation by id = " + id));
        return mapper.toDomain(reservationEntity);
    }

    public List<Reservation> searchAllByFilter(
            ReservationSearchFilter filter
    ) {
        int pageSize = filter.pageSize() != null ? filter.pageSize() : 10;
        int pageNumber = filter.pageNumber() != null ? filter.pageNumber() : 0;
        var pageable = Pageable.ofSize(pageSize).withPage(pageNumber);

        List<ReservationEntity> allEntities = repository.searchAllByFilter(
                filter.roomId(),
                filter.userId(),
                pageable
        );
        return allEntities.stream()
                .map(mapper::toDomain)
                .toList();
    }

    public Reservation createReservation(Reservation reservationToCreate) {
        if (reservationToCreate.status() != null) {
            throw new IllegalArgumentException("Ydali status, balbes. Nado pustoi");
        }

        if (!reservationToCreate.endDate().isAfter(reservationToCreate.startDate())) {
            throw new IllegalArgumentException("Start date must be 1 day earlier then end date");
        }

        var entityToSave = mapper.toEntity(reservationToCreate);
        entityToSave.setStatus(ReservationStatus.PENDING);

        return mapper.toDomain(repository.save(entityToSave));
    }

    public Reservation updateReservation(Long id, Reservation reservationToUpdate) {

        var reservationEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found reservation by id = " + id));

        if (reservationEntity.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalArgumentException("Cannot modify reservation: status = " + reservationEntity.getStatus());
        }
        if (!reservationToUpdate.endDate().isAfter(reservationToUpdate.startDate())) {
            throw new IllegalArgumentException("Start date must be 1 day earlier then end date");
        }

        var reservationToSave = mapper.toEntity(reservationToUpdate);
        reservationToSave.setId(reservationEntity.getId());
        reservationToSave.setStatus(ReservationStatus.PENDING);

        return mapper.toDomain(repository.save(reservationToSave));
    }

    @Transactional
    public void cancelReservation(Long id) {
        var reservation = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found reservation by id = " + id));

        if (reservation.getStatus().equals(ReservationStatus.APPROVED)) {
            throw new IllegalStateException("Cannot cancel approved reservation. Contact manager");
        }

        if (reservation.getStatus().equals(ReservationStatus.CANCELLED)) {
            throw new IllegalStateException("Cannot cancel reservation. Already cancelled");
        }

        repository.setStatus(id, ReservationStatus.CANCELLED);
        log.info("Successfully cancelled reservation: id={}", id);
    }

    public Reservation approveReservation(Long id) {
        var reservationEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found reservation by id = " + id));

        if (reservationEntity.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalArgumentException("Cannot modify reservation: status = " + reservationEntity.getStatus());
        }

        var isAvailableToApprove = service.isReservationAvailable(
                reservationEntity.getRoomId(),
                reservationEntity.getStartDate(),
                reservationEntity.getEndDate()
        );

        if (isAvailableToApprove) {
            throw new IllegalStateException("Cannot approve reservation, conflict");
        }

        reservationEntity.setStatus(ReservationStatus.APPROVED);

        return mapper.toDomain(repository.save(reservationEntity));
    }
}
