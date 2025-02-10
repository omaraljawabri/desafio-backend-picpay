package com.omar.desafio_backend.dtos.request;

import com.omar.desafio_backend.entities.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UserRequestDTO(
        @NotNull(message = "firstName is required")
        @Schema(description = "Primeiro nome do usuário", example = "Fulano", type = "string", requiredMode = Schema.RequiredMode.REQUIRED)
        String firstName,
        @NotNull(message = "lastName is required")
        @Schema(description = "Sobrenome do usuário", example = "de Sousa", type = "string", requiredMode = Schema.RequiredMode.REQUIRED)
        String lastName,
        @Schema(description = "Cpf do usuário", example = "999.999.999-99", type = "string", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String cpf,
        @Schema(description = "Cnpj da empresa do usuário", example = "12.345.678/0001-95", type = "string", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String cnpj,
        @NotNull(message = "email is required")
        @Schema(description = "Email do usuário", example = "fulano@example.com", type = "string", requiredMode = Schema.RequiredMode.REQUIRED)
        @Email
        String email,
        @NotNull(message = "type is required")
        @Schema(description = "Tipo do usuário", example = "MERCHANT", type = "enum", allowableValues = {"MERCHANT", "COMMON"}, requiredMode = Schema.RequiredMode.REQUIRED)
        UserType type,
        @NotNull(message = "balance is required")
        @Schema(description = "Quantia de dinheiro do usuário", example = "1350", type = "BigDecimal", requiredMode = Schema.RequiredMode.REQUIRED)
        BigDecimal balance
) {
}
