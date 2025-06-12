package ru.itis.fisd.semestrovka.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.fisd.semestrovka.entity.dto.PurchaseDto;
import ru.itis.fisd.semestrovka.entity.orm.Purchase;

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

}
