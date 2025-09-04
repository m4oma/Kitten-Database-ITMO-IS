package mappers;

import dto.MistressDto;
import models.Kitten;
import models.Mistress;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class MistressMapper {
    public static MistressDto toDto(Mistress mistress) {
        MistressDto dto = new MistressDto();
        dto.setId(mistress.getId());
        dto.setName(mistress.getName());
        dto.setBirthDate(mistress.getBirthDate());
        dto.setKittenIds(mistress.getKittens().stream().map(Kitten::getId).toList());
        return dto;
    }

    public static Mistress fromDto(MistressDto dto, List<Kitten> kittens) {
        Mistress mistress = new Mistress();
        mistress.setId(dto.getId());
        mistress.setName(dto.getName());
        mistress.setBirthDate(dto.getBirthDate());
        mistress.setKittens(kittens);
        return mistress;
    }
}
