package com.omar.desafio_backend.dtos.response;

import com.omar.desafio_backend.entities.UserType;

public record UserResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String cpf,
        String cnpj,
        String email,
        UserType type
) {
}
