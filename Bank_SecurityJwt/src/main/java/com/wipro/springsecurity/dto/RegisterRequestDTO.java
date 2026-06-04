package com.wipro.springsecurity.dto;

import com.wipro.springsecurity.entity.Role;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class RegisterRequestDTO{
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role; 
}
