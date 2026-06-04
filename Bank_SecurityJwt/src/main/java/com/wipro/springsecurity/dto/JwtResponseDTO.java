package com.wipro.springsecurity.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;


@Data
@AllArgsConstructor
public class JwtResponseDTO {
    private String token;
    private String username;
    @Enumerated(EnumType.STRING)
    private String role;
}
