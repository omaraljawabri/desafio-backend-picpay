package com.omar.desafio_backend.controllers;

import com.omar.desafio_backend.dtos.request.UserRequestDTO;
import com.omar.desafio_backend.dtos.response.UserResponseDTO;
import com.omar.desafio_backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO requestDTO){
        return new ResponseEntity<>(userService.createUser(requestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> findAllUsers(@RequestParam int page, @RequestParam int quantity){
        return ResponseEntity.ok().body(userService.findAll(page, quantity));
    }
}
