package dto;

import models.Kitten;
import models.Mistress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.ExtensionMethod;
import mappers.KittenMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static mappers.KittenMapper.fromDto;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ExtensionMethod(KittenMapper.class)
public class KittenDto {
    private int id;
    private String name;
    private LocalDate birthDate;
    private String colour;
    private int mistressId;
    private List<Integer> friendIds;

    public Kitten getKitten() {
        return fromDto(this, new Mistress(), new ArrayList<>());
    }

    public Kitten getKitten(Mistress mistress, List<Kitten> friends) {
        return fromDto(this, mistress, friends);
    }
}
