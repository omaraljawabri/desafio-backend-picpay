package com.omar.desafio_backend.services;

import com.omar.desafio_backend.dtos.request.UserRequestDTO;
import com.omar.desafio_backend.dtos.response.UserResponseDTO;
import com.omar.desafio_backend.entities.User;
import com.omar.desafio_backend.entities.UserType;
import com.omar.desafio_backend.mappers.UserMapper;
import com.omar.desafio_backend.repositories.UserRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    public UserResponseDTO createUser(UserRequestDTO requestDTO) {
        if (requestDTO.cnpj() == null && requestDTO.cpf() == null){
            throw new ValidationException("Cpf or cpnj should be filled in");
        }
        if ((requestDTO.cnpj() == null && requestDTO.type().equals(UserType.MERCHANT))
        || (requestDTO.cpf() != null && requestDTO.type().equals(UserType.MERCHANT))){
            throw new ValidationException("Merchant should only have cnpj");
        }
        if ((requestDTO.cpf() == null && requestDTO.type().equals(UserType.COMMON))
        || (requestDTO.cnpj() != null && requestDTO.type().equals(UserType.COMMON))){
            throw new ValidationException("Common user should only have cpf");
        }

        if (userRepository.findByEmail(requestDTO.email()).isPresent() || userRepository.findByCpf(requestDTO.cpf()).isPresent()
        || userRepository.findByCnpj(requestDTO.cnpj()).isPresent()){
            throw new ValidationException("Email, cpf or cnpj already exists");
        }

        User savedUser = userRepository.save(mapper.toUser(requestDTO));
        return mapper.toUserResponse(savedUser);
    }

    public Page<UserResponseDTO> findAll(int page, int quantity) {
        return userRepository.findAll(PageRequest.of(page, quantity))
                .map(mapper::toUserResponse);
    }
}
