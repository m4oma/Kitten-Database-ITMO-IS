package ru.m4oma.dto.user;

import jakarta.validation.constraints.NotBlank;

public class LoginUserDto {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
