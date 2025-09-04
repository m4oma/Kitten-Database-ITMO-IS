package ru.m4oma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.ExtensionMethod;
import ru.m4oma.mappers.MistressMapper;

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
    private List<Integer> kittenIds;
}
