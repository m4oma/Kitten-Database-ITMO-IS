package ru.m4oma.dto;

import lombok.experimental.UtilityClass;
import ru.m4oma.model.Mistress;

import java.util.ArrayList;

@UtilityClass
public class MistressMapper {
    public MistressDto toDto(Mistress mistress) {
        return new MistressDto(
                mistress.getId(),
                mistress.getName(),
                mistress.getBirthDate(),
                mistress.getUserId(),
                new ArrayList<>(mistress.getKittenIds())
        );
    }

    public Mistress fromDto(MistressDto dto) {
        Mistress mistress = new Mistress();
        mistress.setId(dto.getId());
        mistress.setName(dto.getName());
        mistress.setBirthDate(dto.getBirthDate());
        mistress.setUserId(dto.getUserId());
        mistress.setKittenIds(dto.getKittenIds() != null ? new ArrayList<>(dto.getKittenIds()) : new ArrayList<>());
        return mistress;
    }
}
