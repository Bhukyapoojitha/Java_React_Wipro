package com.wipro.transaction.entity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)   //  hides transactionId in Swagger
    private Long transactionId;

    private String senderAccount;

    private String receiverAccount;

    private double amount;

    private String type;

    private String status;

   // private boolean suspicious;

    //private int retryCount;

    private LocalDateTime timestamp;
}

