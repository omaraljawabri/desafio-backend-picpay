package com.omar.desafio_backend.repositories;

import com.omar.desafio_backend.entities.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
