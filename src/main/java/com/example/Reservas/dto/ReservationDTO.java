package com.example.Reservas.dto;

import java.time.LocalDateTime;

public record ReservationDTO(Long id, String roomId, LocalDateTime dateTime) {}
