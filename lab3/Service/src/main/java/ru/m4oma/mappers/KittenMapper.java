package ru.m4oma.mappers;

import ru.m4oma.dto.KittenDto;
import lombok.experimental.UtilityClass;
import ru.m4oma.models.Colour;
import ru.m4oma.models.Kitten;
import ru.m4oma.models.Mistress;

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
                kitten.getMistress() != null ? kitten.getMistress().getId() : null,
                kitten.getFriends().stream().map(Kitten::getId).toList()
        );
    }

    public static Kitten fromDto(KittenDto dto, Mistress mistress, List<Kitten> friends) {
        Kitten kitten = new Kitten();
        kitten.setId(dto.getId());
        kitten.setName(dto.getName());
        kitten.setBirthDate(dto.getBirthDate());
        kitten.setTailLength(dto.getTailLength());
        kitten.setColour(Colour.valueOf(dto.getColour()));
        kitten.setMistress(mistress);
        kitten.setFriends(friends);
        return kitten;
    }
}