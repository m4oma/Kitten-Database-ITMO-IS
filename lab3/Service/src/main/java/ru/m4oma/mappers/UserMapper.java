package ru.m4oma.mappers;

import ru.m4oma.dto.UserDto;
import ru.m4oma.models.Mistress;
import ru.m4oma.models.AppUser;
import ru.m4oma.models.Role;

public class UserMapper {
    public static UserDto toDto(AppUser appUser) {
        return new UserDto(
                appUser.getId(),
                appUser.getUsername(),
                null,
                appUser.getRole().name(),
                appUser.getMistress().getId()
        );
    }

    public static AppUser fromDto(UserDto userDto, Mistress mistress) {
        return new AppUser(
                userDto.getUsername(),
                userDto.getPassword(),
                Role.valueOf(userDto.getRole()),
                mistress
        );
    }

    public static AppUser fromDto(UserDto userDto) {
        return new AppUser(
                userDto.getUsername(),
                userDto.getPassword(),
                Role.valueOf(userDto.getRole()),
                null
        );
    }
}
