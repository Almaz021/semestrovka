package ru.itis.fisd.semestrovka.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.fisd.semestrovka.entity.dto.ViewingRequestDto;
import ru.itis.fisd.semestrovka.entity.orm.Apartment;
import ru.itis.fisd.semestrovka.entity.orm.User;
import ru.itis.fisd.semestrovka.entity.orm.ViewingRequest;

@Component
@RequiredArgsConstructor
public class ViewingRequestMapper {

    private final ApartmentMapper apartmentMapper;
    private final UserMapper userMapper;

    public ViewingRequestDto toDto(ViewingRequest request) {
        if (request == null) return null;

        return new ViewingRequestDto(
                request.getId(),
                userMapper.toDto(request.getUser()),
                apartmentMapper.toDto(request.getApartment()),
                request.getPreferredDateTime()
        );
    }

    public ViewingRequest toEntity(ViewingRequestDto dto, User user, Apartment apartment) {
        if (dto == null) return null;

        return ViewingRequest.builder()
                .id(dto.id())
                .user(user)
                .apartment(apartment)
                .preferredDateTime(dto.preferredDateTime())
                .build();
    }
}
