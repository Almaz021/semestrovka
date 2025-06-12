package ru.itis.fisd.semestrovka.mapper;

import org.springframework.stereotype.Component;
import ru.itis.fisd.semestrovka.dto.request.ApartmentFormRequest;
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

    public Apartment toEntity(ApartmentFormRequest request) {
        if (request == null) return null;

        return Apartment.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .address(request.getAddress())
                .price(request.getPrice())
                .rooms(request.getRooms())
                .area(request.getArea())
                .floor(request.getFloor())
                .status(request.getStatus())
                .build();
    }
}
