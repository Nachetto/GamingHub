package dev.nacho.ghub.domain.model.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String username;
    private String password;
}

