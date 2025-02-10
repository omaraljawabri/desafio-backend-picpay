package com.omar.desafio_backend.unit.controllers;

import com.omar.desafio_backend.controllers.UserController;
import com.omar.desafio_backend.dtos.request.UserRequestDTO;
import com.omar.desafio_backend.dtos.response.UserResponseDTO;
import com.omar.desafio_backend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static com.omar.desafio_backend.utils.EntityUtils.*;

@ExtendWith(SpringExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setup(){
        when(userService.createUser(ArgumentMatchers.any(UserRequestDTO.class)))
                .thenReturn(createUserCommonResponseDTO());
        when(userService.findAll(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()))
                .thenReturn(new PageImpl<>(List.of(createUserCommonResponseDTO())));
        when(userService.findById(ArgumentMatchers.anyLong()))
                .thenReturn(createUserCommonResponseDTO());
    }

    @Test
    @DisplayName("createUser should return UserResponseDTO and Http Status 201 when successful")
    void createUser_ReturnUserResponseDTOAndStatus201_WhenSuccessful() {
        ResponseEntity<UserResponseDTO> user
                = userController.createUser(createUserCommonRequestDTO());
        assertThat(user).isNotNull();
        assertThat(user.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(user.getBody()).isNotNull();
    }

    @Test
    @DisplayName("findAllUsers should return Page of UserResponseDTO and Http Status 200 when successful")
    void findAllUsers_ReturnPageOfUserResponseDTOAndStatus200_WhenSuccessful() {
        ResponseEntity<Page<UserResponseDTO>> users = userController.findAllUsers(0, 1);
        assertThat(users).isNotNull();
        assertThat(users.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(users.getBody()).isNotNull();
    }

    @Test
    @DisplayName("findAllUsers should return Page empty and Http Status 200 when there are no users")
    void findAllUsers_ReturnPageEmptyAndHttpStatus200_WhenThereAreNoUsers(){
        when(userService.findAll(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()))
                .thenReturn(Page.empty());
        ResponseEntity<Page<UserResponseDTO>> users = userController.findAllUsers(1, 2);
        assertThat(users).isNotNull();
        assertThat(users.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(users.getBody()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findUserById should return UserResponseDTO and Http Status 200 when successful")
    void findUserById_ReturnUserResponseDTOAndHttpStatus200_WhenSuccessful() {
        ResponseEntity<UserResponseDTO> user = userController.findUserById(1L);
        assertThat(user).isNotNull();
        assertThat(user.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(user.getBody()).isNotNull();
    }
}