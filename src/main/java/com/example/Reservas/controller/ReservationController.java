package com.example.Reservas.controller;

import com.example.Reservas.dto.ReservationDTO;
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
    public ResponseEntity<List<ReservationDTO>> listReservations() {
        try {
            List<ReservationDTO> reservations = reservationService.listReservations().get();
            return ResponseEntity.ok(reservations);
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ReservationDTO> createReservation(@RequestParam String roomId, @RequestParam LocalDateTime dateTime) {
        try {
            ReservationDTO reservation = reservationService.createReservation(roomId, dateTime).get();
            return ResponseEntity.ok(reservation);
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<String> cancelReservation(@PathVariable String id) {
        try {
            boolean canceled = reservationService.cancelReservation(id).get();
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
    public ResponseEntity<List<ReservationDTO>> filterReservationsByDate(@RequestParam LocalDateTime date) {
        try {
            List<ReservationDTO> reservations = reservationService.filterReservationsByDate(date).get();
            return ResponseEntity.ok(reservations);
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }
}
