package ru.m4oma.dto;

import lombok.Data;

@Data
public class KittenFilter {
    private String name;
    private String breed;
    private String colour;
    private Double tailLengthMin;
    private Double tailLengthMax;
    private Integer mistressId;
}