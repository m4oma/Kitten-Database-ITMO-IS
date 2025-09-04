package ru.m4oma.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepaintKittenRequest {
    private int kittenId;
    private String newColor;
}

