package ru.m4oma.security;

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
import ru.m4oma.dto.UserDto;
import ru.m4oma.mappers.UserMapper;
import ru.m4oma.models.AppUser;
import ru.m4oma.models.Mistress;
import ru.m4oma.repositories.AppUserRepository;
import ru.m4oma.repositories.MistressRepository;

@Service
@AllArgsConstructor
public class AuthService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final MistressRepository mistressRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public UserDto register(UserDto userDto) {
        Mistress mistress = mistressRepository.findById(userDto.getMistressId())
                .orElseThrow(() -> new EntityNotFoundException("Mistress not found"));

        appUserRepository.findByUsername(userDto.getUsername()).ifPresent(appUser -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        });
        AppUser appUser = UserMapper.fromDto(userDto, mistress);
        appUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        AppUser savedAppUser = appUserRepository.save(appUser);

        return UserMapper.toDto(savedAppUser);
    }

    public String login(UserDto userDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
        );

        if (!(authentication.getPrincipal() instanceof MyUser myUser)) {
            throw new IllegalStateException("Unexpected principal type: " + authentication.getPrincipal().getClass());
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUser appUser = appUserRepository.findByUsername(userDto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return jwtTokenService.generateToken(appUser);
    }

    public UserDto getCurrentUser(MyUser user) {
        if (user == null) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }

        AppUser appUser = appUserRepository.findById(user.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return UserMapper.toDto(appUser);
    }
}
