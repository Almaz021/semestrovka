package ru.itis.fisd.semestrovka.dto.request;

import jakarta.validation.constraints.Size;

public record ProfileEditRequest(

        @Size(min = 3, max = 50, message = "Username length must be between 3 and 50 characters")
        String username,

        @Size(min = 6, max = 100, message = "Password length must be between 6 and 100 characters")
        String password

) {}
