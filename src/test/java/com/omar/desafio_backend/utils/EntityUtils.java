package com.omar.desafio_backend.utils;

import com.omar.desafio_backend.dtos.request.UserRequestDTO;
import com.omar.desafio_backend.dtos.response.UserResponseDTO;
import com.omar.desafio_backend.entities.User;
import com.omar.desafio_backend.entities.UserType;

import java.math.BigDecimal;

public class EntityUtils {

    public static User createCommonUser(){
        return User.builder().id(1L).firstName("John").lastName("Adam").cpf("999.999.999-99").email("john@example.com")
                .balance(BigDecimal.valueOf(1200)).type(UserType.COMMON).build();
    }

    public static User createMerchantUser(){
        return User.builder().firstName("Lucas").lastName("Smith").cnpj("12.345.678/0001-95").email("lucas@example.com")
                .balance(BigDecimal.valueOf(1500)).type(UserType.MERCHANT).build();
    }

    public static UserRequestDTO createUserCommonRequestDTO(){
        return new UserRequestDTO("John", "Adam", "999.999.999-99", null,"john@example.com",
                UserType.COMMON, BigDecimal.valueOf(1200));
    }

    public static UserRequestDTO createUserMerchantRequestDTO(){
        return new UserRequestDTO("Lucas", "Smith", null, "12.345.678/0001-95", "lucas@example.com",
                UserType.MERCHANT, BigDecimal.valueOf(1500));
    }

    public static UserResponseDTO createUserCommonResponseDTO(){
        return new UserResponseDTO(1L,"John", "Adam", "999.999.999-99", null,"john@example.com",
                UserType.COMMON, BigDecimal.valueOf(1200));
    }

    public static UserResponseDTO createUserMerchantResponseDTO(){
        return new UserResponseDTO(2L,"Lucas", "Smith", null, "12.345.678/0001-95", "lucas@example.com",
                UserType.MERCHANT, BigDecimal.valueOf(1500));
    }
}
