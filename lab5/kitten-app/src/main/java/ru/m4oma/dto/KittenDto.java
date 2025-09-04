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
@ExtensionMethod(KittenMapper.class)
public class KittenDto {
    private int id;

    @NotBlank
    private String name;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    private double tailLength;

    @NotBlank
    private String breed;

    @NotBlank
    private String colour;

    @NotNull
    private Integer mistressId;

    @NotNull
    private List<Integer> friendIds;
}
