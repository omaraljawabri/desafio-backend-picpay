package com.omar.desafio_backend.unit.services;

import com.omar.desafio_backend.dtos.request.UserRequestDTO;
import com.omar.desafio_backend.dtos.response.UserResponseDTO;
import com.omar.desafio_backend.entities.User;
import com.omar.desafio_backend.entities.UserType;
import com.omar.desafio_backend.exceptions.UserNotFoundException;
import com.omar.desafio_backend.mappers.UserMapper;
import com.omar.desafio_backend.repositories.UserRepository;
import com.omar.desafio_backend.services.UserService;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.omar.desafio_backend.utils.EntityUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.when;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper mapper;

    @BeforeEach
    void setup(){
        PageImpl<User> users = new PageImpl<>(List.of(createCommonUser()));
        when(userRepository.save(ArgumentMatchers.any(User.class)))
                .thenReturn(createCommonUser());
        when(userRepository.findByEmail(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());
        when(userRepository.findByCpf(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());
        when(userRepository.findByCnpj(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());
        when(userRepository.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(users);
        when(userRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(createCommonUser()));
        when(mapper.toUser(ArgumentMatchers.any(UserRequestDTO.class)))
                .thenReturn(createCommonUser());
        when(mapper.toUserResponse(ArgumentMatchers.any(User.class)))
                .thenReturn(createUserCommonResponseDTO());
    }

    @Test
    @DisplayName("createUser should return UserResponseDTO and save an user of type Common when successful")
    void createUser_ReturnUserResponseDTOAndSaveUserCommon_WhenSuccessful() {
        UserResponseDTO user = userService.createUser(createUserCommonRequestDTO());
        assertThat(user).isNotNull();
        assertThat(user.type()).isEqualTo(UserType.COMMON);
        assertThat(user.cnpj()).isNull();
        assertThat(user.cpf()).isEqualTo("999.999.999-99");
    }

    @Test
    @DisplayName("createUser should return UserResponseDTO and save and user of type Merchant when successful")
    void createUser_ReturnUserResponseDTOAndSaveUserMerchant_WhenSuccessful(){
        when(userRepository.save(ArgumentMatchers.any(User.class)))
                .thenReturn(createMerchantUser());
        when(mapper.toUser(ArgumentMatchers.any(UserRequestDTO.class)))
                .thenReturn(createMerchantUser());
        when(mapper.toUserResponse(ArgumentMatchers.any(User.class)))
                .thenReturn(createUserMerchantResponseDTO());
        UserResponseDTO user = userService.createUser(createUserMerchantRequestDTO());
        assertThat(user).isNotNull();
        assertThat(user.type()).isEqualTo(UserType.MERCHANT);
        assertThat(user.cnpj()).isEqualTo("12.345.678/0001-95");
        assertThat(user.cpf()).isNull();
    }

    @Test
    @DisplayName("createUser should throw ValidationException when cpf and cpnj are null")
    void createUser_ThrowValidationException_WhenCpfAndCnpjAreNull(){
        UserRequestDTO userRequestDTO = new UserRequestDTO("John", "Adam", null, null, "john@example.com",
                UserType.COMMON, BigDecimal.valueOf(1200));
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> userService.createUser(userRequestDTO))
                .withMessage("Cpf or cpnj should be filled in");
    }

    @Test
    @DisplayName("createUser should throw ValidationException when user is of type MERCHANT and cnpj is null")
    void createUser_ThrowValidationException_WhenUserIsOfTypeMerchantAndCnpjIsNull(){
        UserRequestDTO userRequestDTO = new UserRequestDTO("Lucas", "Smith", "999.999.999-99", null, "lucas@example.com",
                UserType.MERCHANT, BigDecimal.valueOf(1500));
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> userService.createUser(userRequestDTO))
                .withMessage("Merchant should only have cnpj");
    }

    @Test
    @DisplayName("createUser should throw ValidationException when user is of type MERCHANT and cpf is not null")
    void createUser_ThrowValidationException_WhenUserIsOfTypeMerchantAndCpfIsNotNull(){
        UserRequestDTO userRequestDTO = new UserRequestDTO("Lucas", "Smith", "999.999.999-99", "12.345.678/0001-95", "lucas@example.com",
                UserType.MERCHANT, BigDecimal.valueOf(1500));
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> userService.createUser(userRequestDTO))
                .withMessage("Merchant should only have cnpj");
    }

    @Test
    @DisplayName("createUser should throw ValidationException when user is of type COMMON and cpf is null")
    void createUser_ThrowValidationException_WhenUserIsOfTypeCommonAndCpfIsNull(){
        UserRequestDTO userRequestDTO = new UserRequestDTO("John", "Adam", null, "12.345.678/0001-95", "john@example.com",
                UserType.COMMON, BigDecimal.valueOf(1200));
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> userService.createUser(userRequestDTO))
                .withMessage("Common user should only have cpf");
    }

    @Test
    @DisplayName("createUser should throw ValidationException when user is of type COMMON and cnpj is not null")
    void createUser_ThrowValidationException_WhenUserIsOfTypeCommonAndCnpjIsNotNull(){
        UserRequestDTO userRequestDTO = new UserRequestDTO("John", "Adam", "999.999.999-99", "12.345.678/0001-95", "john@example.com",
                UserType.COMMON, BigDecimal.valueOf(1200));
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> userService.createUser(userRequestDTO))
                .withMessage("Common user should only have cpf");
    }

    @Test
    @DisplayName("createUser should throw ValidationException when user email already exists")
    void createUser_ThrowValidationException_WhenUserEmailAlreadyExists(){
        when(userRepository.findByEmail(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(createCommonUser()));
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> userService.createUser(createUserCommonRequestDTO()))
                .withMessage("email already exists");
    }

    @Test
    @DisplayName("createUser should throw ValidationException when user is of type COMMON and cpf already exists")
    void createUser_ThrowValidationException_WhenUserIsOfTypeCommonAndCpfAlreadyExists(){
        when(userRepository.findByCpf(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(createCommonUser()));
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> userService.createUser(createUserCommonRequestDTO()))
                .withMessage("cpf already exists");
    }

    @Test
    @DisplayName("createUser should throw ValidationException when user is of type MERCHANT and cnpj already exists")
    void createUser_ThrowValidationException_WhenUserIsOfTypeMerchantAndCnpjAlreadyExists(){
        when(userRepository.findByCnpj(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(createMerchantUser()));
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> userService.createUser(createUserMerchantRequestDTO()))
                .withMessage("cnpj already exists");
    }

    @Test
    @DisplayName("findAll should return Page of UserResponseDTO when successful")
    void findAll_ReturnPageOfUserResponseDTO_WhenSuccessful() {
        Page<UserResponseDTO> userResponseDTOS = userService.findAll(0, 1);
        assertThat(userResponseDTOS.getContent()).isNotNull().isNotEmpty().hasSize(1);
        assertThat(userResponseDTOS.getContent().get(0).id()).isEqualTo(1L);
        assertThat(userResponseDTOS.getContent().get(0).type()).isEqualTo(UserType.COMMON);
    }

    @Test
    @DisplayName("findById should return UserResponseDTO when successful")
    void findById_ReturnUserResponseDTO_WhenSuccessful() {
        UserResponseDTO userResponseDTO = userService.findById(1L);
        assertThat(userResponseDTO).isNotNull();
        assertThat(userResponseDTO.id()).isEqualTo(1L);
        assertThat(userResponseDTO.type()).isEqualTo(UserType.COMMON);
    }

    @Test
    @DisplayName("findById should throw UserNotFoundException when user with that id does not exists")
    void findById_ThrowUserNotFoundException_WhenUserWithThatIdDoesNotExists(){
        when(userRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> userService.findById(9L))
                .withMessage("User with id: 9 not found");
    }

    @Test
    @DisplayName("findByIdReturnUser should return User when successful")
    void findByIdReturnUser_ReturnUser_WhenSuccessful() {
        User user = userService.findByIdReturnUser(1L);
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getType()).isEqualTo(UserType.COMMON);
    }

    @Test
    @DisplayName("findByIdReturnUser should throw UserNotFoundException when user with that id does not exists")
    void findByIdReturnUser_ThrowUserNotFoundException_WhenUserWithThatIdDoesNotExists(){
        when(userRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> userService.findByIdReturnUser(9L))
                .withMessage("User with id: 9 not found");
    }

    @Test
    @DisplayName("saveUser should save an user into database when successful")
    void saveUser_SaveUserIntoDatabase_WhenSuccessful() {
        assertThatCode(() -> userService.saveUser(createCommonUser()))
                .doesNotThrowAnyException();
    }
}