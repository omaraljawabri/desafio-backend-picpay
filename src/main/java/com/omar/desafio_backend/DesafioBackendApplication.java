package com.omar.desafio_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DesafioBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesafioBackendApplication.class, args);
	}

}
