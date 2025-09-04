package ru.m4oma.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.ExtensionMethod;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ExtensionMethod(MistressMapper.class)
public class MistressDto {
    private int id;

    private String name;

    private LocalDate birthDate;

    private Integer userId;

    private List<Integer> kittenIds;
}
