package ru.m4oma.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateKittenRequest {
    private String name;

    private LocalDate birthDate;

    private double tailLength;

    private String breed;

    private String colour;

    private Integer mistressId;

    private List<Integer> friendIds;
}
