package com.omar.desafio_backend.services;

import com.omar.desafio_backend.dtos.request.UserRequestDTO;
import com.omar.desafio_backend.dtos.response.UserResponseDTO;
import com.omar.desafio_backend.entities.User;
import com.omar.desafio_backend.entities.UserType;
import com.omar.desafio_backend.exceptions.UserNotFoundException;
import com.omar.desafio_backend.mappers.UserMapper;
import com.omar.desafio_backend.repositories.UserRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

        if (userRepository.findByEmail(requestDTO.email()).isPresent()){
            throw new ValidationException("email already exists");
        }

        if (requestDTO.type().equals(UserType.COMMON) && userRepository.findByCpf(requestDTO.cpf()).isPresent()){
            throw new ValidationException("cpf already exists");
        }

        if (requestDTO.type().equals(UserType.MERCHANT) && userRepository.findByCnpj(requestDTO.cnpj()).isPresent()){
            throw new ValidationException("cnpj already exists");
        }

        User savedUser = userRepository.save(mapper.toUser(requestDTO));
        return mapper.toUserResponse(savedUser);
    }

    public Page<UserResponseDTO> findAll(int page, int quantity) {
        return userRepository.findAll(PageRequest.of(page, quantity))
                .map(mapper::toUserResponse);
    }

    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id: %d not found", id)));
        return mapper.toUserResponse(user);
    }

    public User findByIdReturnUser(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id: %d not found", id)));
    }

    public void saveUser(User user){
        userRepository.save(user);
    }
}
