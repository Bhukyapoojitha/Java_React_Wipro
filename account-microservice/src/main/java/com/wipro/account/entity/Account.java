package com.wipro.account.entity;
//package com.wipro jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "accounts")
public class Account {

    @Id   // ✅ Primary Key
    private String accountNo;

    private Double balance;   // ✅ only required field
}

