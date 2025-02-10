package com.omar.desafio_backend.unit.controllers;

import com.omar.desafio_backend.controllers.TransferController;
import com.omar.desafio_backend.dtos.request.TransferRequestDTO;
import com.omar.desafio_backend.dtos.response.TransferResponseDTO;
import com.omar.desafio_backend.services.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static com.omar.desafio_backend.utils.EntityUtils.*;

@ExtendWith(SpringExtension.class)
class TransferControllerTest {

    @InjectMocks
    private TransferController transferController;

    @Mock
    private TransferService transferService;

    @BeforeEach
    void setup(){
        when(transferService.createTransfer(ArgumentMatchers.any(TransferRequestDTO.class)))
                .thenReturn(createTransferResponseDTO());
    }

    @Test
    @DisplayName("createTransfer should return TransferResponseDTO and Http Status 201 when successful")
    void createTransfer_ReturnTransferResponseDTOAndStatus201_WhenSuccessful() {
        ResponseEntity<TransferResponseDTO> transfer =
                transferController.createTransfer(createTransferRequestDTO());
        assertThat(transfer).isNotNull();
        assertThat(transfer.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(transfer.getBody()).isNotNull();
    }
}