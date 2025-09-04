package ru.m4oma.dto.user;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequestDto {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotNull
    private String role;

    @NotNull
    private int mistressId;
}
