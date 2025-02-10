package com.omar.desafio_backend.mappers;

import com.omar.desafio_backend.dtos.request.UserRequestDTO;
import com.omar.desafio_backend.dtos.response.UserResponseDTO;
import com.omar.desafio_backend.entities.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public User toUser(UserRequestDTO requestDTO){
        return User.builder()
                .firstName(requestDTO.firstName())
                .lastName(requestDTO.lastName())
                .email(requestDTO.email())
                .cpf(requestDTO.cpf())
                .cnpj(requestDTO.cnpj())
                .type(requestDTO.type())
                .build();
    }

    public UserResponseDTO toUserResponse(User user){
        return new UserResponseDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getCpf(),
                user.getCnpj(), user.getEmail(), user.getType());
    }
}
