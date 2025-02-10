package com.omar.desafio_backend.services;

import com.omar.desafio_backend.clients.AuthorizeClient;
import com.omar.desafio_backend.clients.NotificationClient;
import com.omar.desafio_backend.dtos.request.TransferRequestDTO;
import com.omar.desafio_backend.dtos.response.AuthorizeResponseDTO;
import com.omar.desafio_backend.dtos.response.NotificationResponseDTO;
import com.omar.desafio_backend.dtos.response.TransferResponseDTO;
import com.omar.desafio_backend.entities.Transfer;
import com.omar.desafio_backend.entities.User;
import com.omar.desafio_backend.entities.UserType;
import com.omar.desafio_backend.exceptions.BusinessException;
import com.omar.desafio_backend.exceptions.ExternalRequestErrorException;
import com.omar.desafio_backend.exceptions.InsufficientBalanceException;
import com.omar.desafio_backend.mappers.TransferMapper;
import com.omar.desafio_backend.repositories.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferRepository transferRepository;
    private final AuthorizeClient authorizeClient;
    private final NotificationClient notificationClient;
    private final UserService userService;
    private final TransferMapper mapper;

    @Transactional
    public TransferResponseDTO createTransfer(TransferRequestDTO requestDTO) {
        User payer = userService.findByIdReturnUser(requestDTO.payer());
        if (payer.getType().equals(UserType.MERCHANT)){
            throw new BusinessException("User of type Merchant cannot be a payer");
        }
        if (payer.getBalance().compareTo(requestDTO.value()) < 0){
            throw new InsufficientBalanceException("Payer balance is lesser than transfer value");
        }
        if (!validateAuthorization() || !sendNotification()){
            throw new ExternalRequestErrorException();
        }
        User payee = userService.findByIdReturnUser(requestDTO.payee());
        payer.setBalance(payer.getBalance().subtract(requestDTO.value()));
        payee.setBalance(payee.getBalance().add(requestDTO.value()));
        userService.saveUser(payer);
        userService.saveUser(payee);
        Transfer transfer = saveTransfer(payer, payee, requestDTO);
        return mapper.toTransferResponse(transfer, payer, payee);
    }

    private boolean validateAuthorization(){
        ResponseEntity<AuthorizeResponseDTO> authorization = authorizeClient.getAuthorization();
        return authorization.getStatusCode().equals(HttpStatus.OK);
    }

    private boolean sendNotification(){
        ResponseEntity<NotificationResponseDTO> notification = notificationClient.sendNotification();
        return notification.getStatusCode().equals(HttpStatus.NO_CONTENT);
    }

    private Transfer saveTransfer(User payer, User payee, TransferRequestDTO transferRequestDTO){
        Transfer transfer = Transfer.builder().value(transferRequestDTO.value()).sender(payer).receiver(payee)
                .build();
        return transferRepository.save(transfer);
    }
}
