package ru.itis.fisd.semestrovka.mapper;

import org.springframework.stereotype.Component;
import ru.itis.fisd.semestrovka.entity.dto.CallbackRequestDto;
import ru.itis.fisd.semestrovka.entity.orm.CallbackRequest;

@Component
public class CallbackRequestMapper {

    public CallbackRequestDto toDto(CallbackRequest entity) {
        if (entity == null) return null;

        return new CallbackRequestDto(
                entity.getId(),
                entity.getName(),
                entity.getPhone(),
                entity.getStatus(),
                entity.getRequestedAt()
        );
    }

    public static CallbackRequest toEntity(CallbackRequestDto dto) {
        if (dto == null) return null;

        return CallbackRequest.builder()
                .id(dto.id())
                .name(dto.name())
                .phone(dto.phone())
                .status(dto.status())
                .requestedAt(dto.requestedAt())
                .build();
    }
}
