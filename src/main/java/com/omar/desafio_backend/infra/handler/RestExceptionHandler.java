package com.omar.desafio_backend.infra.handler;

import com.omar.desafio_backend.exceptions.BusinessException;
import com.omar.desafio_backend.exceptions.ExternalRequestErrorException;
import com.omar.desafio_backend.exceptions.InsufficientBalanceException;
import com.omar.desafio_backend.exceptions.UserNotFoundException;
import feign.FeignException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    private ResponseEntity<ErrorMessage> handler(ValidationException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorMessage.builder()
                        .timestamp(LocalDateTime.now())
                        .title("Validation Exception")
                        .message(exception.getMessage())
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .status(HttpStatus.BAD_REQUEST.value())
                        .build()
        );
    }

    @ExceptionHandler(FeignException.class)
    private ResponseEntity<ErrorMessage> handler(FeignException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorMessage.builder()
                        .timestamp(LocalDateTime.now())
                        .title("Feign Exception")
                        .message(exception.getMessage())
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .status(HttpStatus.BAD_REQUEST.value())
                        .build()
        );
    }

    @ExceptionHandler(BusinessException.class)
    private ResponseEntity<ErrorMessage> handler(BusinessException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorMessage.builder()
                        .timestamp(LocalDateTime.now())
                        .title("Business Exception")
                        .message(exception.getMessage())
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .status(HttpStatus.BAD_REQUEST.value())
                        .build()
        );
    }

    @ExceptionHandler(ExternalRequestErrorException.class)
    private ResponseEntity<ErrorMessage> handler(ExternalRequestErrorException exception){
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(
                ErrorMessage.builder()
                        .timestamp(LocalDateTime.now())
                        .title("External Request Error Exception")
                        .message(exception.getMessage())
                        .httpStatus(HttpStatus.BAD_GATEWAY)
                        .status(HttpStatus.BAD_GATEWAY.value())
                        .build()
        );
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    private ResponseEntity<ErrorMessage> handler(InsufficientBalanceException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorMessage.builder()
                        .timestamp(LocalDateTime.now())
                        .title("Insufficient Balance Exception")
                        .message(exception.getMessage())
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .status(HttpStatus.BAD_REQUEST.value())
                        .build()
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<ErrorMessage> handler(UserNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorMessage.builder()
                        .timestamp(LocalDateTime.now())
                        .title("User Not Found Exception")
                        .message(exception.getMessage())
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .status(HttpStatus.NOT_FOUND.value())
                        .build()
        );
    }
}
