package com.omar.desafio_backend.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferResponseDTO(
        @Schema(description = "Valor da transferência", type = "BigDecimal", example = "1200")
        BigDecimal value,
        @Schema(description = "Momento em que a transferência ocorreu", type = "datetime", example = "2025-02-10T18:50:08.704856")
        LocalDateTime timestamp,
        @Schema(description = "Dados do pagador", type = "UserResponseDTO")
        UserResponseDTO payer,
        @Schema(description = "Dados do destinatário", type = "UserResponseDTO")
        UserResponseDTO payee
) {
}
