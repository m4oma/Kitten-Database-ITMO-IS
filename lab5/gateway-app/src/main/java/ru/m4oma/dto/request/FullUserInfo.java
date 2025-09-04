package ru.m4oma.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.m4oma.dto.KittenDto;
import ru.m4oma.dto.MistressDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullUserInfo {
    private String username;
    private String role;
    private MistressDto mistress;
    private List<KittenDto> kittens;
}
