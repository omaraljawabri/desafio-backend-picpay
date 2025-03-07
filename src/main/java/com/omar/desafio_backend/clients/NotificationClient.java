package com.omar.desafio_backend.clients;

import com.omar.desafio_backend.dtos.response.NotificationResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "notification-client",
        url = "${notify-client.url}"
)
public interface NotificationClient {

    @PostMapping
    ResponseEntity<NotificationResponseDTO> sendNotification();
}
