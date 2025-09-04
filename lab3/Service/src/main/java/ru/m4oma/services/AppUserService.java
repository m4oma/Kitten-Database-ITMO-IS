package ru.m4oma.services;

import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.m4oma.dto.UserDto;
import ru.m4oma.mappers.UserMapper;
import ru.m4oma.models.AppUser;
import ru.m4oma.repositories.AppUserRepository;

@Transactional
@ExtensionMethod(UserMapper.class)
@Service
public class AppUserService {
    AppUserRepository appUserRepository;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    public UserDto getById(int id) {
        AppUser user = appUserRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.toDto(user);
    }

    @Transactional
    public UserDto save(UserDto userDto) {
        AppUser user = UserMapper.fromDto(userDto);
        return UserMapper.toDto(appUserRepository.save(user));
    }
}
