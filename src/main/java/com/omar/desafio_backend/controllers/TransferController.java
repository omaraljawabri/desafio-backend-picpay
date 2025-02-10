package com.omar.desafio_backend.controllers;

import com.omar.desafio_backend.dtos.request.TransferRequestDTO;
import com.omar.desafio_backend.dtos.response.TransferResponseDTO;
import com.omar.desafio_backend.services.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<TransferResponseDTO> createTransfer(@RequestBody TransferRequestDTO requestDTO){
        return new ResponseEntity<>(transferService.createTransfer(requestDTO), HttpStatus.CREATED);
    }
}
