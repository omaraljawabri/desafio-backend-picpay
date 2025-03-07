package com.omar.desafio_backend.clients;

import com.omar.desafio_backend.dtos.response.AuthorizeResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "authorize-client",
        url = "${authorize-client.url}"
)
public interface AuthorizeClient {

    @GetMapping
    ResponseEntity<AuthorizeResponseDTO> getAuthorization();

}
