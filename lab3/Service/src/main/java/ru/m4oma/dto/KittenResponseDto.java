package ru.m4oma.dto;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class KittenResponseDto {

    private String name;

    private LocalDate birthDate;

    private double tailLength;

    private String breed;

    private String colour;

    private Integer mistressId;
}
