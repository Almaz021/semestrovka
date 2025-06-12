package ru.itis.fisd.semestrovka.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseListRequest {

    @Min(value = 0, message = "Номер страницы не может быть отрицательным")
    private int page = 0;

    @Min(value = 1, message = "Размер страницы должен быть минимум 1")
    @Max(value = 100, message = "Размер страницы не может превышать 100")
    private int size = 10;

}
