package dto;

import models.Kitten;
import models.Mistress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.ExtensionMethod;
import mappers.MistressMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static mappers.MistressMapper.fromDto;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ExtensionMethod(MistressMapper.class)
public class MistressDto {
    private int id;
    private String name;
    private LocalDate birthDate;
    private List<Integer> kittenIds;

    public Mistress getMistress() {
        return fromDto(this, new ArrayList<>());
    }

    public Mistress getKitten(List<Kitten> kittens) {
        return fromDto(this, kittens);
    }
}
