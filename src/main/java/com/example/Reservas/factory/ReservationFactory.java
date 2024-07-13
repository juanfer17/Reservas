package com.example.Reservas.factory;

import com.example.Reservas.model.Reservation;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReservationFactory {

    public static Reservation createReservation(String roomId, LocalDateTime dateTime) {
        return new Reservation(null, roomId, dateTime);
    }
}