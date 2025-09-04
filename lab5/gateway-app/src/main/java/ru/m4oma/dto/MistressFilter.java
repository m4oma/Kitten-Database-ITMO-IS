package ru.m4oma.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
public class MistressFilter {
    private String name;
    private LocalDate birthDateAfter;
    private LocalDate birthDateBefore;
}
