package ru.m4oma.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.ExtensionMethod;
import ru.m4oma.dto.mapper.UserMapper;
import ru.m4oma.model.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ExtensionMethod(UserMapper.class)
public class AppUserDto {
    private int id;

    private String username;

    private Role role;
}
