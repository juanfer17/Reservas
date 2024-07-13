package com.example.Reservas.service;

import com.example.Reservas.factory.ReservationFactory;
import com.example.Reservas.model.Reservation;
import com.example.Reservas.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository = ReservationRepository.getInstance();
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private static final AtomicLong idGenerator = new AtomicLong();

    public Future<List<Reservation>> listReservations() {
        return executorService.submit(reservationRepository::findAll);
    }

    public Future<Reservation> createReservation(String roomId, LocalDateTime dateTime) {
        return executorService.submit(() -> {
            Reservation reservation = ReservationFactory.createReservation(roomId, dateTime);
            synchronized (reservationRepository) {
                if (isRoomAvailable(roomId, dateTime)) {
                    reservation.setId(generateUniqueId());
                    return reservationRepository.save(reservation);
                } else {
                    throw new IllegalArgumentException("Sala no está disponible");
                }
            }
        });
    }


    public Future<Boolean> cancelReservation(String id) {
        return executorService.submit(() -> {
            synchronized (reservationRepository) {
                if (reservationRepository.existsByRoomId(id)) {
                    reservationRepository.deleteByRoomId(id);
                    return true;
                } else {
                    throw new IllegalArgumentException("Reserva no encontrada");
                }
            }
        });
    }

    public Future<List<Reservation>> filterReservationsByDate(LocalDateTime date) {
        return executorService.submit(() -> reservationRepository.findByDateTimeBetween(date.withHour(0).withMinute(0).withSecond(0),
                date.withHour(23).withMinute(59).withSecond(59)));
    }

    private boolean isRoomAvailable(String roomId, LocalDateTime dateTime) {
        List<Reservation> reservations = reservationRepository.findAll();
        for (Reservation reservation : reservations) {
            if (reservation.getRoomId().equals(roomId) && reservation.getDateTime().equals(dateTime)) {
                return false;
            }
        }
        return true;
    }

    private Long generateUniqueId() {
        return idGenerator.incrementAndGet();
    }
}
