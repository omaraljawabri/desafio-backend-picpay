package com.omar.desafio_backend.unit.services;

import com.omar.desafio_backend.clients.AuthorizeClient;
import com.omar.desafio_backend.clients.NotificationClient;
import com.omar.desafio_backend.dtos.request.TransferRequestDTO;
import com.omar.desafio_backend.dtos.response.TransferResponseDTO;
import com.omar.desafio_backend.entities.Transfer;
import com.omar.desafio_backend.entities.User;
import com.omar.desafio_backend.exceptions.BusinessException;
import com.omar.desafio_backend.exceptions.ExternalRequestErrorException;
import com.omar.desafio_backend.exceptions.InsufficientBalanceException;
import com.omar.desafio_backend.mappers.TransferMapper;
import com.omar.desafio_backend.repositories.TransferRepository;
import com.omar.desafio_backend.services.TransferService;
import com.omar.desafio_backend.services.UserService;
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

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static com.omar.desafio_backend.utils.EntityUtils.*;

@ExtendWith(SpringExtension.class)
class TransferServiceTest {

    @InjectMocks
    private TransferService transferService;

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private AuthorizeClient authorizeClient;

    @Mock
    private NotificationClient notificationClient;

    @Mock
    private UserService userService;

    @Mock
    private TransferMapper mapper;

    @BeforeEach
    void setup(){
        when(userService.findByIdReturnUser(ArgumentMatchers.anyLong()))
                .thenReturn(createCommonUser());
        when(authorizeClient.getAuthorization())
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        when(notificationClient.sendNotification())
                .thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));
        when(transferRepository.save(ArgumentMatchers.any(Transfer.class)))
                .thenReturn(createTransfer());
        when(mapper.toTransferResponse(ArgumentMatchers.any(Transfer.class), ArgumentMatchers.any(User.class), ArgumentMatchers.any(User.class)))
                .thenReturn(createTransferResponseDTO());
        doNothing().when(userService).saveUser(ArgumentMatchers.any(User.class));
    }

    @Test
    @DisplayName("createTransfer should return TransferResponseDTO when successful")
    void createTransfer_ReturnTransferResponseDTO_WhenSuccessful() {
        TransferResponseDTO transfer = transferService.createTransfer(createTransferRequestDTO());
        assertThat(transfer).isNotNull();
        assertThat(transfer.value()).isEqualTo(BigDecimal.valueOf(1200));
        assertThat(transfer.payee()).isNotNull();
        assertThat(transfer.payer()).isNotNull();
    }

    @Test
    @DisplayName("createTransfer should throw BusinessException when payer is of type MERCHANT")
    void createTransfer_ThrowBusinessException_WhenPayerIsOfTypeMerchant(){
        when(userService.findByIdReturnUser(ArgumentMatchers.anyLong()))
                .thenReturn(createMerchantUser());
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> transferService.createTransfer(createTransferRequestDTO()))
                .withMessage("User of type Merchant cannot be a payer");
    }

    @Test
    @DisplayName("createTransfer should throw InsufficientBalanceException when payer balance is lesser than transfer value")
    void createTransfer_ThrowInsufficientBalanceException_WhenPayerBalanceIsLesserThanTransferValue(){
        TransferRequestDTO transferRequestDTO = new TransferRequestDTO(BigDecimal.valueOf(1800), 1L, 2L);
        assertThatExceptionOfType(InsufficientBalanceException.class)
                .isThrownBy(() -> transferService.createTransfer(transferRequestDTO))
                .withMessage("Payer balance is lesser than transfer value");
    }

    @Test
    @DisplayName("createTransfer should throw ExternalRequestErrorException when authorization is not validated")
    void createTransfer_ThrowExternalRequestErrorException_WhenAuthorizationIsNotValidated(){
        when(authorizeClient.getAuthorization())
                .thenReturn(new ResponseEntity<>(HttpStatus.FORBIDDEN));
        assertThatExceptionOfType(ExternalRequestErrorException.class)
                .isThrownBy(() -> transferService.createTransfer(createTransferRequestDTO()))
                .withMessage("Error while trying to make a request to an external service");
    }

    @Test
    @DisplayName("createTransfer should throw ExternalRequestErrorException when notification is not sent")
    void createTransfer_ThrowExternalRequestErrorException_WhenNotificationIsNotSent(){
        when(notificationClient.sendNotification())
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_GATEWAY));
        assertThatExceptionOfType(ExternalRequestErrorException.class)
                .isThrownBy(() -> transferService.createTransfer(createTransferRequestDTO()))
                .withMessage("Error while trying to make a request to an external service");
    }

    @Test
    @DisplayName("createTransfer should throw ExternalRequestErrorException when notification is not sent and authorization is not validated")
    void createTransfer_ThrowExternalRequestErrorException_WhenNotificationIsNotSentAndAuthorizationIsNotValidated(){
        when(authorizeClient.getAuthorization())
                .thenReturn(new ResponseEntity<>(HttpStatus.FORBIDDEN));
        when(notificationClient.sendNotification())
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_GATEWAY));
        assertThatExceptionOfType(ExternalRequestErrorException.class)
                .isThrownBy(() -> transferService.createTransfer(createTransferRequestDTO()))
                .withMessage("Error while trying to make a request to an external service");
    }
}