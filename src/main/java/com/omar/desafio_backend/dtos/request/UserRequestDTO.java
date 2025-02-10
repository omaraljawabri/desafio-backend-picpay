package com.omar.desafio_backend.dtos.request;

import com.omar.desafio_backend.entities.UserType;
import jakarta.validation.constraints.NotNull;

public record UserRequestDTO(
        @NotNull(message = "firstName is required")
        String firstName,
        @NotNull(message = "lastName is required")
        String lastName,
        String cpf,
        String cnpj,
        @NotNull(message = "email is required")
        String email,
        @NotNull(message = "type is required")
        UserType type
) {
}
