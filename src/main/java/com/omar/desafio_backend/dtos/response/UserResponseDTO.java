package com.omar.desafio_backend.dtos.response;

import com.omar.desafio_backend.entities.UserType;

import java.math.BigDecimal;

public record UserResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String cpf,
        String cnpj,
        String email,
        UserType type,
        BigDecimal balance
) {
}
