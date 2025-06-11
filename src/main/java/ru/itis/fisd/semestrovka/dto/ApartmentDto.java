package ru.itis.fisd.semestrovka.dto;

import lombok.Data;
import ru.itis.fisd.semestrovka.entity.orm.Apartment;

import java.math.BigDecimal;

@Data
public class ApartmentDto {
    private Long id;
    private String title;
    private String description;
    private String address;
    private Integer price;

    private Integer rooms;
    private BigDecimal area;
    private Integer floor;

    public static ApartmentDto from(Apartment apartment) {
        ApartmentDto dto = new ApartmentDto();
        dto.setId(apartment.getId());
        dto.setTitle(apartment.getTitle());
        if (apartment.getDescription() != null) {
            dto.setDescription(apartment.getDescription());
        } else {
            dto.setDescription("Описание отсутствует");
        }
        dto.setAddress(apartment.getAddress());
        dto.setPrice(apartment.getPrice());

        dto.setRooms(apartment.getRooms());
        dto.setArea(apartment.getArea());
        dto.setFloor(apartment.getFloor());

        return dto;
    }
}
