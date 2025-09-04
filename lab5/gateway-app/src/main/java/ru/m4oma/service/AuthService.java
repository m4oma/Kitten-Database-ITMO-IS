package ru.m4oma.service;


import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.m4oma.communication.KittenClient;
import ru.m4oma.communication.MistressClient;
import ru.m4oma.dto.*;
import ru.m4oma.dto.mapper.UserMapper;
import ru.m4oma.dto.request.FullUserInfo;
import ru.m4oma.dto.user.LoginRequestDto;
import ru.m4oma.dto.user.RegisterUserRequestDto;
import ru.m4oma.dto.user.TokenResponseDto;
import ru.m4oma.dto.user.UserResponseDto;
import ru.m4oma.model.AppUser;
import ru.m4oma.repository.AppUserRepository;
import ru.m4oma.security.JwtTokenService;
import ru.m4oma.security.MyUserDetails;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final MistressClient mistressClient;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final KittenClient kittenClient;

    public UserResponseDto register(RegisterUserRequestDto userDto) {
        MistressDto mistress = mistressClient.getMistressById(userDto.getMistressId())
                .orElseThrow(() -> new EntityNotFoundException("Mistress not found"));
        appUserRepository.findByUsername(userDto.getUsername()).ifPresent(u -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        });
        AppUser appUser = UserMapper.fromDto(userDto, mistress.getId());
        appUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        AppUser savedAppUser = appUserRepository.save(appUser);

        return UserMapper.toResponseDto(savedAppUser);
    }


    public TokenResponseDto login(LoginRequestDto dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AppUser user = appUserRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        return new TokenResponseDto(jwtTokenService.generateToken(user));
    }


    public UserResponseDto getCurrentUser(MyUserDetails userDetails) {
        if (userDetails == null) {
            throw new AccessDeniedException("You do not have enough rights");
        }
        AppUser user = appUserRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return UserMapper.toResponseDto(user);
    }

    public FullUserInfo getCurrentUserFull(MyUserDetails userDetails) {
        AppUser user = userDetails.getUser();
        MistressDto mistress = mistressClient.getMistressById(user.getMistressId())
                .orElseThrow(() -> new RuntimeException("Mistress not found"));
        List<KittenDto> kittens = kittenClient.getKittensByMistressId(user.getMistressId());

        return new FullUserInfo(
                user.getUsername(),
                user.getRole().name(),
                mistress,
                kittens
        );
    }
}

