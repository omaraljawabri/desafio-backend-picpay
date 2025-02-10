package com.omar.desafio_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class ExternalRequestErrorException extends RuntimeException {

    public ExternalRequestErrorException(){
        super("Error while trying to make a request to an external service");
    }

    public ExternalRequestErrorException(String message) {
        super(message);
    }
}
