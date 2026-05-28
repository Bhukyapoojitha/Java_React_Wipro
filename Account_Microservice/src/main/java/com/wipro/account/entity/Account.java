package com.wipro.account.entity;

import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;   

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)   // hides accountId in Swagger
    private Long accountId;

    private String accountNumber;

    private String accountHolderName;

    private double balance;

    private String accountType;

    private String status;
}

