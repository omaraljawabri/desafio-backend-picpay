package com.omar.desafio_backend.integration;

import com.omar.desafio_backend.dtos.request.UserRequestDTO;
import com.omar.desafio_backend.dtos.response.UserResponseDTO;
import com.omar.desafio_backend.entities.User;
import com.omar.desafio_backend.entities.UserType;
import com.omar.desafio_backend.repositories.UserRepository;
import com.omar.desafio_backend.testcontainer.TestcontainersConfig;
import com.omar.desafio_backend.wrapper.PageableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;

import static com.omar.desafio_backend.utils.EntityUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerIT {

    private static final String ROOT_URL = "/user";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("createUser should return a UserResponseDTO and http status 201 when successful")
    void createUser_ReturnUserResponseDTOAndStatus201_WhenSuccessful() {
        ResponseEntity<UserResponseDTO> responseEntity
                = testRestTemplate.postForEntity(ROOT_URL, new HttpEntity<>(createUserRequestDTO(), null), UserResponseDTO.class);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("createUser should return http status 400 when both cpf and cnpj are null")
    void createUser_ReturnStatus400_WhenCpfAndCnpjAreNull(){
        UserRequestDTO userRequestDTO = new UserRequestDTO("John", "Adam", null, null, "john@example.com",
                UserType.COMMON, BigDecimal.valueOf(1200D));
        ResponseEntity<String> responseEntity
                = testRestTemplate.postForEntity(ROOT_URL, new HttpEntity<>(userRequestDTO, null), String.class);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).contains("Cpf or cpnj should be filled in");
    }

    @Test
    @DisplayName("createUser should return http status 400 when cnpj of user of type merchant is null")
    void createUser_ReturnStatus400_WhenCnpjOfUserOfTypeMerchantIsNull(){
        UserRequestDTO userRequestDTO = new UserRequestDTO("John", "Adam", "999.999.999-99", null, "john@example.com",
                UserType.MERCHANT, BigDecimal.valueOf(1200D));
        ResponseEntity<String> responseEntity
                = testRestTemplate.postForEntity(ROOT_URL, new HttpEntity<>(userRequestDTO, null), String.class);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).contains("Merchant should only have cnpj");
    }

    @Test
    @DisplayName("createUser should return http status 400 when cpf of user of type common is null")
    void createUser_ReturnStatus400_WhenCpfOfUserOfTypeCommonIsNull(){
        UserRequestDTO userRequestDTO = new UserRequestDTO("John", "Adam", null, "12.345.678/0001-95", "john@example.com",
                UserType.COMMON, BigDecimal.valueOf(1200D));
        ResponseEntity<String> responseEntity
                = testRestTemplate.postForEntity(ROOT_URL, new HttpEntity<>(userRequestDTO, null), String.class);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).contains("Common user should only have cpf");
    }

    @Test
    @DisplayName("createUser should return http status 400 when user email already exists")
    void createUser_ReturnStatus400_WhenUserEmailAlreadyExists(){
        User commonUser = createCommonUser();
        commonUser.setId(null);
        userRepository.save(commonUser);
        ResponseEntity<String> responseEntity
                = testRestTemplate.postForEntity(ROOT_URL, new HttpEntity<>(createUserRequestDTO(), null), String.class);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).contains("email already exists");
    }

    @Test
    @DisplayName("createUser should return http status 400 when user cpf already exists")
    void createUser_ReturnStatus400_WhenUserCpfAlreadyExists(){
        User commonUser = createCommonUser();
        commonUser.setEmail("test@example.com");
        commonUser.setId(null);
        userRepository.save(commonUser);
        ResponseEntity<String> responseEntity
                = testRestTemplate.postForEntity(ROOT_URL, new HttpEntity<>(createUserRequestDTO(), null), String.class);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).contains("cpf already exists");
    }

    @Test
    @DisplayName("createUser should return http status 400 when user cnpj already exists")
    void createUser_ReturnStatus400_WhenUserCnpjAlreadyExists(){
        User merchantUser = createMerchantUser();
        merchantUser.setEmail("test@example.com");
        userRepository.save(merchantUser);
        UserRequestDTO userRequestDTO = new UserRequestDTO("John", "Adam", null, "12.345.678/0001-95", "john@example.com",
                UserType.MERCHANT, BigDecimal.valueOf(1200D));
        ResponseEntity<String> responseEntity
                = testRestTemplate.postForEntity(ROOT_URL, new HttpEntity<>(userRequestDTO, null), String.class);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).contains("cnpj already exists");
    }

    @Test
    @DisplayName("findAllUsers should return a paginated list of UserResponseDTO and http status 200 when successful")
    void findAllUsers_ReturnPaginatedListOfUserResponseDTOAndStatus200_WhenSuccessful() {
        userRepository.save(createMerchantUser());
        User commonUser = createCommonUser();
        commonUser.setId(null);
        userRepository.save(commonUser);
        ResponseEntity<PageableResponse<UserResponseDTO>> responseEntity
                = testRestTemplate.exchange(ROOT_URL + "?page=0&quantity=1", HttpMethod.GET, new HttpEntity<>(null, null), new ParameterizedTypeReference<PageableResponse<UserResponseDTO>>() {
        });
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull().isNotEmpty().hasSize(1);
    }

    @Test
    @DisplayName("findAllUsers should return an empty list and http status 200 when there are no users in the system")
    void findAllUsers_ReturnEmptyListAndStatus200_WhenThereAreNoUsers(){
        ResponseEntity<PageableResponse<UserResponseDTO>> responseEntity
                = testRestTemplate.exchange(ROOT_URL + "?page=0&quantity=1", HttpMethod.GET, new HttpEntity<>(null, null), new ParameterizedTypeReference<PageableResponse<UserResponseDTO>>() {
        });
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findUserById should return a UserResponseDTO and http status 200 when successful")
    void findUserById_ReturnUserResponseDTOAndStatus200_WhenSuccessful() {
        userRepository.save(createMerchantUser());
        ResponseEntity<UserResponseDTO> responseEntity
                = testRestTemplate.exchange(ROOT_URL + "/{id}", HttpMethod.GET, new HttpEntity<>(null, null), UserResponseDTO.class, 1L);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("findUserById should return http status 404 when there is no user with the passed id")
    void findUserById_ReturnStatus404_WhenThereIsNoUserWithPassedId(){
        ResponseEntity<Void> responseEntity
                = testRestTemplate.exchange(ROOT_URL + "/{id}", HttpMethod.GET, new HttpEntity<>(null, null), Void.class, 1L);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}