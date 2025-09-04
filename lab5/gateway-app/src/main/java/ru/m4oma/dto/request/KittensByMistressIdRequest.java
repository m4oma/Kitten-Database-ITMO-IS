package ru.m4oma.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KittensByMistressIdRequest {
    private Integer mistressId;
}
