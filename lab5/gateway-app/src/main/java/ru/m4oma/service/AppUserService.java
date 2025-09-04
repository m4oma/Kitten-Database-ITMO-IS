package ru.m4oma.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.m4oma.dto.user.UpdateUserRequestDto;
import ru.m4oma.dto.user.UserResponseDto;
import ru.m4oma.model.AppUser;
import ru.m4oma.model.Role;
import ru.m4oma.repository.AppUserRepository;
import java.util.List;

import ru.m4oma.dto.mapper.UserMapper;


@Service
@RequiredArgsConstructor
@Transactional
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto getById(int id) {
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return UserMapper.toResponseDto(user);
    }

    public UserResponseDto save(int id, UpdateUserRequestDto updateDto) {
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (updateDto.getUsername() != null) {
            user.setUsername(updateDto.getUsername());
        }

        if (updateDto.getPassword() != null && !updateDto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(updateDto.getPassword()));
        }

        if (updateDto.getRole() != null) {
            user.setRole(Role.valueOf(updateDto.getRole()));
        }

        return UserMapper.toResponseDto(appUserRepository.save(user));
    }

    public void deleteById(int id) {
        if (!appUserRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found");
        }
        appUserRepository.deleteById(id);
    }

    public List<UserResponseDto> getAll() {
        return appUserRepository.findAll().stream()
                .map(UserMapper::toResponseDto)
                .toList();
    }
}
