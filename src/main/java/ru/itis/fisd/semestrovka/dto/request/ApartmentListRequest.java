package ru.itis.fisd.semestrovka.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApartmentListRequest {

    @Min(value = 0, message = "Страница не меньше 0")
    private int page = 1;

    @Min(value = 1, message = "Размер страницы должен быть не меньше 1")
    @Max(value = 100, message = "Размер страницы не должен превышать 100")
    private int size = 10;

    @Pattern(regexp = "id|name|price")
    private String sort = "id";

    @Pattern(regexp = "asc|desc", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String dir = "asc";

}
