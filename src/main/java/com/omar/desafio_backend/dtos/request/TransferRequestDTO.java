package com.omar.desafio_backend.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferRequestDTO(
        @NotNull(message = "value is required")
        @Schema(description = "Valor da transferência", type = "BigDecimal", example = "1200", requiredMode = Schema.RequiredMode.REQUIRED)
        BigDecimal value,
        @NotNull(message = "payer is required")
        @Schema(description = "Id do pagador", type = "Long", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long payer,
        @NotNull(message = "payee is required")
        @Schema(description = "Id do destinatário", type = "Long", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
        Long payee
) {
}
