package mappers;

import dto.KittenDto;
import models.Kitten;
import models.Mistress;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class KittenMapper {
    public static KittenDto toDto(Kitten kitten) {
        KittenDto dto = new KittenDto();
        dto.setId(kitten.getId());
        dto.setName(kitten.getName());
        dto.setBirthDate(kitten.getBirthDate());
        dto.setColour(kitten.getColour().name());
        dto.setMistressId(kitten.getMistress() != null ? kitten.getMistress().getId() : null);
        dto.setFriendIds(kitten.getFriends().stream().map(Kitten::getId).toList());
        return dto;
    }

    public static Kitten fromDto(KittenDto dto, Mistress mistress, List<Kitten> friends) {
        Kitten kitten = new Kitten();
        kitten.setId(dto.getId());
        kitten.setName(dto.getName());
        kitten.setBirthDate(dto.getBirthDate());
        kitten.setColour(Enum.valueOf(models.Colour.class, dto.getColour()));
        kitten.setMistress(mistress);
        kitten.setFriends(friends);
        return kitten;
    }
}
