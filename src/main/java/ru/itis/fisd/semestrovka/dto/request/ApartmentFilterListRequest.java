package ru.itis.fisd.semestrovka.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApartmentFilterListRequest {

    @Min(value = 0, message = "Минимальная цена не может быть меньше 0")
    private Integer minPrice = 0;

    @Min(value = 0, message = "Максимальная цена не может быть меньше 0")
    @Max(value = 1_000_000, message = "Максимальная цена слишком велика")
    private Integer maxPrice = 10000;

    private String sort = "asc";

    @Min(value = 0, message = "Номер страницы не может быть отрицательным")
    private Integer page = 0;

    @Min(value = 1, message = "Размер страницы должен быть хотя бы 1")
    @Max(value = 100, message = "Слишком большой размер страницы")
    private Integer size = 10;
}
