package ru.itis.fisd.semestrovka.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.fisd.semestrovka.entity.dto.PurchaseDto;
import ru.itis.fisd.semestrovka.entity.orm.Apartment;
import ru.itis.fisd.semestrovka.entity.orm.Purchase;
import ru.itis.fisd.semestrovka.entity.orm.User;

@Component
@RequiredArgsConstructor
public class PurchaseMapper {

    private final ApartmentMapper apartmentMapper;
    private final UserMapper userMapper;

    public PurchaseDto toDto(Purchase entity) {
        if (entity == null) return null;

        return new PurchaseDto(
                entity.getId(),
                userMapper.toDto(entity.getUser()),
                apartmentMapper.toDto(entity.getApartment()),
                entity.getPurchaseDate()
        );
    }

    public Purchase toEntity(PurchaseDto dto, User user, Apartment apartment) {
        if (dto == null || user == null || apartment == null) return null;

        return Purchase.builder()
                .id(dto.id())
                .user(user)
                .apartment(apartment)
                .purchaseDate(dto.purchaseDate())
                .build();
    }
}
