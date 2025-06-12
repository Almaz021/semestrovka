package ru.itis.fisd.semestrovka.entity.dto;

import java.time.LocalDateTime;

public record CallbackRequestDto(
        Long id,
        String name,
        String phone,
        String status,
        LocalDateTime requestedAt
) {
}
