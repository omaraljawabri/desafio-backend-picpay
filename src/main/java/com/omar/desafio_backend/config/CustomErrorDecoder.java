package com.omar.desafio_backend.config;

import com.omar.desafio_backend.exceptions.ExternalRequestErrorException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        if (response.status() >= 400 && response.status() < 600){
            return new ExternalRequestErrorException();
        }

        return new Default().decode(s, response);
    }
}
