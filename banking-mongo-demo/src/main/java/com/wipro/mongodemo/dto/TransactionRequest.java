package com.wipro.mongodemo.dto;

import com.wipro.mongodemo.model.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "Request body to perform a transaction")
public class TransactionRequest {

    @Schema(example = "DEPOSIT", required = true)
    private TransactionType type;

    @Schema(example = "500.00", required = true)
    private BigDecimal amount;

    @Schema(example = "Salary credited")
    private String description;

    @Schema(example = "ACC-0002", description = "Required only for TRANSFER type")
    private String targetAccountNumber;
}
