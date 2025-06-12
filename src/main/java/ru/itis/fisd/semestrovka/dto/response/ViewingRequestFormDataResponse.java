package ru.itis.fisd.semestrovka.dto.response;

import ru.itis.fisd.semestrovka.entity.dto.ApartmentDto;

import java.time.LocalDateTime;
import java.util.List;

public record ViewingRequestFormDataResponse(ApartmentDto apartment, List<LocalDateTime> availableSlots) {
}
