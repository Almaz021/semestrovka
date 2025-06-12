package ru.itis.fisd.semestrovka.entity.dto;


import java.util.Set;

public record UserDto(
        Long id,
        String username,
        String role,
        Set<ApartmentDto> favoriteApartments
) {
}
