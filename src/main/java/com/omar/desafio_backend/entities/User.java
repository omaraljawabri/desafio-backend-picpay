package com.omar.desafio_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "tb_user")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String cpf;

    private String cnpj;

    private String email;

    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private UserType type;

    @OneToMany(mappedBy = "sender")
    private List<Transfer> transferSender;

    @OneToMany(mappedBy = "receiver")
    private List<Transfer> transferReceiver;
}
