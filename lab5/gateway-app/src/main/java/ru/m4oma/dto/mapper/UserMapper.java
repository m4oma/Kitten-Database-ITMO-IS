package ru.m4oma.dto.mapper;

import ru.m4oma.dto.user.*;
import ru.m4oma.model.AppUser;
import ru.m4oma.model.Role;

public class UserMapper {
    public static AppUser fromDto(RegisterUserRequestDto dto, int mistressId) {
        return new AppUser(
                0,
                dto.getUsername(),
                dto.getPassword(),
                Role.valueOf(dto.getRole()),
                mistressId
        );
    }

    public static RegisterUserRequestDto toRegisterUserRequestDto(AppUser user) {
        return new RegisterUserRequestDto(
                user.getUsername(),
                user.getPassword(),
                user.getRole().name(),
                user.getMistressId()
        );
    }

    public static UserDto toUserDto(AppUser user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                null,
                user.getRole().name(),
                user.getMistressId()
        );
    }

    public static AppUserDto toAppUserDto(AppUser user) {
        return new AppUserDto(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }


    public static AppUser fromRegisterDto(RegisterUserRequestDto dto) {
        return new AppUser(
                0,
                dto.getUsername(),
                dto.getPassword(),
                Role.valueOf(dto.getRole()),
                dto.getMistressId()
        );
    }

    public static UserResponseDto toResponseDto(AppUser user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getRole().name(),
                user.getMistressId()
        );
    }

    public static UpdateUserRequestDto toUpdateDto(AppUser user) {
        return new UpdateUserRequestDto(
                user.getUsername(),
                null,
                user.getRole().name()
        );
    }

}
