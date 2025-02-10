package com.omar.desafio_backend.clients;

import com.omar.desafio_backend.dtos.response.AuthorizeResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "authorize-client",
        url = "https://util.devi.tools/api/v2/authorize"
)
public interface AuthorizeClient {

    @GetMapping
    ResponseEntity<AuthorizeResponseDTO> getAuthorization();

}
