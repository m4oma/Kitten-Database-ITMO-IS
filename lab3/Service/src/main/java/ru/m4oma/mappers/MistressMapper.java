package ru.m4oma.mappers;

import ru.m4oma.dto.MistressDto;
import lombok.experimental.UtilityClass;
import ru.m4oma.models.Kitten;
import ru.m4oma.models.Mistress;

import java.util.List;

@UtilityClass
public class MistressMapper {
    public static MistressDto toDto(Mistress mistress) {
        return new MistressDto(
                mistress.getId(),
                mistress.getName(),
                mistress.getBirthDate(),
                mistress.getKittens().stream().map(Kitten::getId).toList()
        );
    }

    public static Mistress fromDto(MistressDto dto, List<Kitten> kittens) {
        Mistress mistress = new Mistress();
        mistress.setId(dto.getId());
        mistress.setName(dto.getName());
        mistress.setBirthDate(dto.getBirthDate());
        mistress.setKittens(kittens);
        return mistress;
    }

    public static Mistress fromDto(MistressDto dto) {
        Mistress mistress = new Mistress();
        mistress.setId(dto.getId());
        mistress.setName(dto.getName());
        mistress.setBirthDate(dto.getBirthDate());
        return mistress;
    }
}
