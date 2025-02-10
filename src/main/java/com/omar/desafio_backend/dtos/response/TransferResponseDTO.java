package com.omar.desafio_backend.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferResponseDTO(
        BigDecimal value,
        LocalDateTime timestamp,
        UserResponseDTO payer,
        UserResponseDTO payee
) {
}
