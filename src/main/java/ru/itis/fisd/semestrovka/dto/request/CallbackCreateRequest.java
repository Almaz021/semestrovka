package ru.itis.fisd.semestrovka.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CallbackCreateRequest {

    @NotBlank(message = "Имя обязательно для заполнения")
    @Size(max = 100, message = "Имя слишком длинное")
    private String name;

    @NotBlank(message = "Телефон обязателен")
    @Pattern(regexp = "^\\+7\\d{10}$", message = "Номер должен быть в формате +7XXXXXXXXXX")
    private String phone;
}
