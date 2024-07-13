package com.example.Reservas.controller;

import com.example.Reservas.model.Reservation;
import com.example.Reservas.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/reservations")
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<Reservation>> listReservations() {
        try {
            List<Reservation> reservations = reservationService.listReservations().get();
            return ResponseEntity.ok(reservations);
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Reservation> createReservation(@RequestParam String roomId, @RequestParam LocalDateTime dateTime) {
        try {
            Reservation reservation = reservationService.createReservation(roomId, dateTime).get();
            return ResponseEntity.ok(reservation);
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @DeleteMapping("/cancel/{roomId}")
    public ResponseEntity<String> cancelReservation(@PathVariable String roomId) {
        try {
            boolean canceled = reservationService.cancelReservation(roomId).get();
            if (canceled) {
                return ResponseEntity.ok("Reserva cancelada exitosamente");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al cancelar la reserva: " + e.getMessage());
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Reservation>> filterReservationsByDate(@RequestParam LocalDateTime date) {
        try {
            List<Reservation> reservations = reservationService.filterReservationsByDate(date).get();
            return ResponseEntity.ok(reservations);
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }
}