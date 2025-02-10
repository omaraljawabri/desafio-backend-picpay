package com.omar.desafio_backend.exceptions;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(){
        super("Insufficient balance to make this transaction");
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
