package com.omar.desafio_backend.dtos.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferRequestDTO(
        @NotNull(message = "value is required")
        BigDecimal value,
        @NotNull(message = "payer is required")
        Long payer,
        @NotNull(message = "payee is required")
        Long payee
) {
}
