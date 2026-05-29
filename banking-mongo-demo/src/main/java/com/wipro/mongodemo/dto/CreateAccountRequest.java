package com.wipro.mongodemo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "Request body to create a new bank account")
public class CreateAccountRequest {

    @Schema(example = "John Doe", required = true)
    private String ownerName;

    @Schema(example = "john@example.com", required = true)
    private String email;

    @Schema(example = "SAVINGS", allowableValues = {"SAVINGS", "CURRENT"})
    private String accountType;

    @Schema(example = "1000.00", description = "Initial deposit amount")
    private BigDecimal initialDeposit;
}
