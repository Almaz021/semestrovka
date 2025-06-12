package ru.itis.fisd.semestrovka.entity.dto;

import java.math.BigDecimal;

public record ApartmentDto(
        Long id,
        String title,
        String description,
        String address,
        Integer price,
        Integer rooms,
        BigDecimal area,
        Integer floor,
        String status
) {
}
