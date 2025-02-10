package com.omar.desafio_backend.dtos.response;

import com.omar.desafio_backend.entities.UserType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record UserResponseDTO(
        @Schema(description = "Id do usuário", example = "1", type = "long")
        Long id,
        @Schema(description = "Primeiro nome do usuário", example = "Fulano", type = "string")
        String firstName,
        @Schema(description = "Sobrenome do usuário", example = "de Sousa", type = "string")
        String lastName,
        @Schema(description = "Cpf do usuário", example = "999.999.999-99", type = "string")
        String cpf,
        @Schema(description = "Cnpj da empresa do usuário", example = "12.345.678/0001-95", type = "string")
        String cnpj,
        @Schema(description = "Email do usuário", example = "fulano@example.com", type = "string")
        String email,
        @Schema(description = "Tipo do usuário", example = "COMMON", allowableValues = {"MERCHANT", "COMMON"}, type = "enum")
        UserType type,
        @Schema(description = "Quantidade de dinheiro do usuário", example = "1200", type = "BigDecimal")
        BigDecimal balance
) {
}
