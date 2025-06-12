package ru.itis.fisd.semestrovka.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoritesListRequest {

    @Min(value = 0, message = "Page must be greater or equal to 0")
    private int page = 0;

    @Min(value = 1, message = "Size must be greater or equal to 1")
    private int size = 5;
}
