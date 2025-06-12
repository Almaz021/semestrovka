package ru.itis.fisd.semestrovka.entity.dto;

import java.time.LocalDateTime;

public record ViewingRequestDto(
        Long id,
        UserDto user,
        ApartmentDto apartment,
        LocalDateTime preferredDateTime
) {
}
