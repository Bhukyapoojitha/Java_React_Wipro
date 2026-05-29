package com.wipro.mongodemo.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "accounts")
public class Account {

    @Id
    private String id;

    @Indexed(unique = true)
    private String accountNumber;

    private String ownerName;
    private String email;
    private String accountType;        // SAVINGS, CURRENT
    private BigDecimal balance;
    private LocalDateTime createdAt;

    @Version                           // optimistic locking
    private Long version;

    private List<Transaction> transactions = new ArrayList<>();
}
