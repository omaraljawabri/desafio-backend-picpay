package com.omar.desafio_backend.utils;

import com.omar.desafio_backend.dtos.request.TransferRequestDTO;
import com.omar.desafio_backend.dtos.request.UserRequestDTO;
import com.omar.desafio_backend.dtos.response.TransferResponseDTO;
import com.omar.desafio_backend.dtos.response.UserResponseDTO;
import com.omar.desafio_backend.entities.Transfer;
import com.omar.desafio_backend.entities.User;
import com.omar.desafio_backend.entities.UserType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EntityUtils {

    public static User createCommonUser(){
        return User.builder().id(1L).firstName("John").lastName("Adam").cpf("999.999.999-99").email("john@example.com")
                .balance(BigDecimal.valueOf(1200)).type(UserType.COMMON).build();
    }

    public static User createMerchantUser(){
        return User.builder().firstName("Lucas").lastName("Smith").cnpj("12.345.678/0001-95").email("lucas@example.com")
                .balance(BigDecimal.valueOf(1500)).type(UserType.MERCHANT).build();
    }

    public static UserRequestDTO createUserRequestDTO(){
        return new UserRequestDTO("John", "Adam", "999.999.999-99", null,"john@example.com",
                UserType.COMMON, BigDecimal.valueOf(1200D));
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

    public static Transfer createTransfer(){
        return Transfer.builder().id(1L).value(BigDecimal.valueOf(1200)).timestamp(LocalDateTime.now())
                .sender(createCommonUser()).receiver(createMerchantUser()).build();
    }

    public static TransferRequestDTO createTransferRequestDTO(){
        return new TransferRequestDTO(BigDecimal.valueOf(1200), 1L, 2L);
    }

    public static TransferResponseDTO createTransferResponseDTO(){
        return new TransferResponseDTO(BigDecimal.valueOf(1200), LocalDateTime.now(),
                createUserCommonResponseDTO(), createUserMerchantResponseDTO());
    }
}
