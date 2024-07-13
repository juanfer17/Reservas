package com.example.Reservas.repository;

import com.example.Reservas.model.Reservation;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ReservationRepository {

    private static ReservationRepository instance;
    private final List<Reservation> reservations = new ArrayList<>();
    private final Object lock = new Object();

    public ReservationRepository() {}

    public static synchronized ReservationRepository getInstance() {
        if (instance == null) {
            instance = new ReservationRepository();
        }
        return instance;
    }

    public List<Reservation> findAll() {
        synchronized (lock) {
            return new ArrayList<>(reservations);
        }
    }

    public Reservation save(Reservation reservation) {
        synchronized (lock) {
            reservations.add(reservation);
        }
        return reservation;
    }

    public void deleteByRoomId(String roomId) {
        synchronized (lock) {
            reservations.removeIf(reservation -> reservation.getRoomId().equals(roomId));
        }
    }

    public boolean existsByRoomId(String roomId) {
        synchronized (lock) {
            return reservations.stream().anyMatch(reservation -> reservation.getRoomId().equals(roomId));
        }
    }

    public List<Reservation> findByDateTimeBetween(LocalDateTime start, LocalDateTime end) {
        synchronized (lock) {
            return reservations.stream()
                    .filter(reservation -> reservation.getDateTime().isAfter(start) && reservation.getDateTime().isBefore(end))
                    .collect(Collectors.toList());
        }
    }
}
