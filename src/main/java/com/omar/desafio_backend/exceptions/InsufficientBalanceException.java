package com.omar.desafio_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(){
        super("Insufficient balance to make this transaction");
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
