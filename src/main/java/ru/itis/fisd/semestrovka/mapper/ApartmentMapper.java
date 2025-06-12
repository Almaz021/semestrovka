package ru.itis.fisd.semestrovka.mapper;

import org.springframework.stereotype.Component;
import ru.itis.fisd.semestrovka.entity.dto.ApartmentDto;
import ru.itis.fisd.semestrovka.entity.orm.Apartment;

@Component
public class ApartmentMapper {

    public ApartmentDto toDto(Apartment apartment) {
        if (apartment == null) return null;

        return new ApartmentDto(
                apartment.getId(),
                apartment.getTitle(),
                apartment.getDescription(),
                apartment.getAddress(),
                apartment.getPrice(),
                apartment.getRooms(),
                apartment.getArea(),
                apartment.getFloor(),
                apartment.getStatus()
        );
    }

    public Apartment toEntity(ApartmentDto dto) {
        if (dto == null) return null;

        return Apartment.builder()
                .id(dto.id())
                .title(dto.title())
                .description(dto.description())
                .address(dto.address())
                .price(dto.price())
                .rooms(dto.rooms())
                .area(dto.area())
                .floor(dto.floor())
                .status(dto.status())
                .build();
    }
}
