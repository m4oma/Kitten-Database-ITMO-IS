package ru.m4oma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MistressDto {
    private int id;

    private String name;

    private LocalDate birthDate;

    private Integer userId;

    private List<Integer> kittenIds;
}