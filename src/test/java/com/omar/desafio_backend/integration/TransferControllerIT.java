package com.omar.desafio_backend.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.omar.desafio_backend.dtos.response.TransferResponseDTO;
import com.omar.desafio_backend.entities.User;
import com.omar.desafio_backend.repositories.TransferRepository;
import com.omar.desafio_backend.repositories.UserRepository;
import com.omar.desafio_backend.testcontainer.TestcontainersConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static com.omar.desafio_backend.utils.EntityUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource("classpath:application-test.properties")
class TransferControllerIT {

    private static final String ROOT_URL = "/transfer";

    private WireMockServer wireMockServer;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransferRepository transferRepository;

    @BeforeEach
    void setupWireMock(){
        wireMockServer = new WireMockServer(9090);
        wireMockServer.start();
        WireMock.configureFor("localhost", 9090);
    }

    @AfterEach
    void tearDownWireMock() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Test
    @DisplayName("createTransfer should return a TransferResponseDTO and http status 201 when successful")
    void createTransfer_ReturnTransferResponseDTOAndStatus201_WhenSuccessful() {
        mockExternalAPIAuthorize("/api/v2/authorize", 200);
        mockExternalAPINotify("/api/v1/notify", 204);
        User commonUser = createCommonUser();
        commonUser.setId(null);
        userRepository.save(commonUser);
        userRepository.save(createMerchantUser());
        ResponseEntity<TransferResponseDTO> responseEntity
                = testRestTemplate.postForEntity(ROOT_URL, new HttpEntity<>(createTransferRequestDTO(), null), TransferResponseDTO.class);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().payer().id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("createTransfer should return http status 400 when user of type merchant is a payer")
    void createTransfer_ReturnStatus400_WhenUserOfTypeMerchantIsAPayer(){
        userRepository.save(createMerchantUser());
        User commonUser = createCommonUser();
        commonUser.setId(null);
        userRepository.save(commonUser);
        ResponseEntity<String> responseEntity
                = testRestTemplate.postForEntity(ROOT_URL, new HttpEntity<>(createTransferRequestDTO(), null), String.class);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).contains("User of type Merchant cannot be a payer");
    }

    @Test
    @DisplayName("createTransfer should return http status 400 when payer balance is lesser than transfer value")
    void createTransfer_ReturnStatus400_WhenPayerBalanceIsLesserThanTransferValue(){
        User commonUser = createCommonUser();
        commonUser.setId(null);
        commonUser.setBalance(BigDecimal.valueOf(1000));
        userRepository.save(commonUser);
        userRepository.save(createMerchantUser());
        ResponseEntity<String> responseEntity
                = testRestTemplate.postForEntity(ROOT_URL, new HttpEntity<>(createTransferRequestDTO(), null), String.class);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).contains("Payer balance is lesser than transfer value");
    }

    @Test
    @DisplayName("createTransfer should return http status 502 when the external authorization API does not return http status 200")
    void createTransfer_ReturnStatus502_WhenExternalAuthorizationAPIDoesNotReturnStatus200(){
        mockExternalAPIAuthorize("/api/v2/authorize", 400);
        mockExternalAPINotify("/api/v1/notify", 204);
        User commonUser = createCommonUser();
        commonUser.setId(null);
        userRepository.save(commonUser);
        userRepository.save(createMerchantUser());
        ResponseEntity<String> responseEntity
                = testRestTemplate.postForEntity(ROOT_URL, new HttpEntity<>(createTransferRequestDTO(), null), String.class);
        assertThat(responseEntity).isNotNull();
        System.out.println(responseEntity.getBody());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).contains("Error while trying to make a request to an external service");
    }

    @Test
    @DisplayName("createTransfer should return http status 502 when the external notification API does not return http status 204")
    void createTransfer_ReturnStatus502_WhenExternalNotificationAPIDoesNotReturnStatus204(){
        mockExternalAPIAuthorize("/api/v2/authorize", 200);
        mockExternalAPINotify("/api/v1/notify", 500);
        User commonUser = createCommonUser();
        commonUser.setId(null);
        userRepository.save(commonUser);
        userRepository.save(createMerchantUser());
        ResponseEntity<String> responseEntity
                = testRestTemplate.postForEntity(ROOT_URL, new HttpEntity<>(createTransferRequestDTO(), null), String.class);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).contains("Error while trying to make a request to an external service");
    }

    private void mockExternalAPIAuthorize(String url, int status){
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo(url))
                .willReturn(WireMock.aResponse()
                        .withStatus(status)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"message\": \"Success\"}")));
    }

    private void mockExternalAPINotify(String url, int status){
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(url))
                .willReturn(WireMock.aResponse()
                        .withStatus(status)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"message\": \"Success\"}")));
    }
}