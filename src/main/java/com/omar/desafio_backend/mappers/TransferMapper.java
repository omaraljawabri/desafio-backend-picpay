package com.omar.desafio_backend.mappers;

import com.omar.desafio_backend.dtos.response.TransferResponseDTO;
import com.omar.desafio_backend.entities.Transfer;
import com.omar.desafio_backend.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransferMapper {

    private final UserMapper userMapper;

    public TransferResponseDTO toTransferResponse(Transfer transfer, User payer, User payee){
        return new TransferResponseDTO(transfer.getValue(), transfer.getTimestamp(), userMapper.toUserResponse(payer),
                userMapper.toUserResponse(payee));
    }
}
