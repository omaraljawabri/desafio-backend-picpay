package com.omar.desafio_backend.exceptions;

public class ExternalRequestErrorException extends RuntimeException {

    public ExternalRequestErrorException(){
        super("Error while trying to make a request to an external service");
    }

    public ExternalRequestErrorException(String message) {
        super(message);
    }
}
