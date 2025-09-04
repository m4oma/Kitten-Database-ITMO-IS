package ru.m4oma.dto;

import lombok.experimental.UtilityClass;
import ru.m4oma.model.Breed;
import ru.m4oma.model.Colour;
import ru.m4oma.model.Kitten;

import java.util.List;

@UtilityClass
public class KittenMapper {
    public static KittenDto toDto(Kitten kitten) {
        return new KittenDto(
                kitten.getId(),
                kitten.getName(),
                kitten.getBirthDate(),
                kitten.getTailLength(),
                kitten.getBreed().name(),
                kitten.getColour().name(),
                kitten.getMistressId() != null ? kitten.getMistressId() : null,
                kitten.getFriends().stream().map(Kitten::getId).toList()
        );
    }

    public static Kitten fromDto(KittenDto dto, List<Kitten> friends) {
        Kitten kitten = new Kitten();
        kitten.setId(dto.getId());
        kitten.setName(dto.getName());
        kitten.setBirthDate(dto.getBirthDate());
        kitten.setTailLength(dto.getTailLength());
        kitten.setBreed(Breed.valueOf(dto.getBreed().toUpperCase()));
        kitten.setColour(Colour.valueOf(dto.getColour().toUpperCase()));
        kitten.setMistressId(dto.getMistressId());
        kitten.setFriends(friends);
        return kitten;
    }
}