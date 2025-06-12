package ru.itis.fisd.semestrovka.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.fisd.semestrovka.entity.dto.ApartmentDto;
import ru.itis.fisd.semestrovka.entity.dto.UserDto;
import ru.itis.fisd.semestrovka.entity.orm.User;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ApartmentMapper apartmentMapper;

    public UserDto toDto(User user) {
        if (user == null) return null;

        Set<ApartmentDto> apartmentIds = user.getFavoriteApartments().stream()
                .map(apartmentMapper::toDto).collect(Collectors.toSet());

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                apartmentIds
        );
    }

}
