package ru.itis.fisd.semestrovka.entity.dto;



import java.time.LocalDateTime;

public record PurchaseDto(
        Long id,
        UserDto user,
        ApartmentDto apartment,
        LocalDateTime purchaseDate
) {
}