package ru.itis.fisd.semestrovka.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CallbackListRequest {

    @Min(value = 0, message = "Номер страницы не может быть отрицательным")
    private int page = 0;

    @Min(value = 1, message = "Размер страницы должен быть не меньше 1")
    @Max(value = 100, message = "Размер страницы не должен превышать 100")
    private int size = 10;

}
