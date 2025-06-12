package ru.itis.fisd.semestrovka.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ApartmentFormRequest {
    @NotBlank(message = "Название не должно быть пустым")
    private String title;

    @Size(max = 1000, message = "Описание не должно превышать 1000 символов")
    private String description;

    @NotBlank(message = "Адрес обязателен")
    private String address;

    @NotNull(message = "Цена обязательна")
    @Min(value = 1, message = "Цена должна быть положительной")
    private Integer price;

    @NotNull(message = "Количество комнат обязательно")
    @Min(value = 1, message = "Минимум 1 комната")
    @Max(value = 10, message = "Максимум 10 комнат")
    private Integer rooms;

    @NotNull(message = "Площадь обязательна")
    @DecimalMin(value = "10.0", message = "Площадь должна быть не менее 10 м²")
    private BigDecimal area;

    @NotNull(message = "Этаж обязателен")
    @Min(value = 1, message = "Этаж должен быть положительным")
    private Integer floor;

    @NotBlank(message = "Статус обязателен")
    private String status;
}
