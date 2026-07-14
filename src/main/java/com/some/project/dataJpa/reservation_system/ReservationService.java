package com.some.project.dataJpa.reservation_system;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReservationService {

    private final Map<Long, Reservation> reservationMap;
    private final AtomicLong idCounter;

    public ReservationService() {
        this.idCounter = new AtomicLong();
        this.reservationMap = new HashMap<>();
    }

    public Reservation getReservationById(Long id) {
        if (!reservationMap.containsKey(id)) {
            throw  new NoSuchElementException("Not found reservauion with ud = " + id);
        }
        return  reservationMap.get(id);
    }

    public List<Reservation> findAllReservations() {
        return reservationMap.values().stream().toList();
    };

    public Reservation createReservation(Reservation reservationToCreate) {

    }
}
