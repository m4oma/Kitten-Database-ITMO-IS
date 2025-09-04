package ru.m4oma.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.m4oma.dto.AppUserDto;
import ru.m4oma.models.AppUser;
import ru.m4oma.repositories.AppUserRepository;
import ru.m4oma.security.MyUser;

@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    private final AppUserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<AppUserDto> getCurrentUser(Authentication authentication) {
        MyUser user = (MyUser) authentication.getPrincipal();
        AppUser appUser = user.getUser();
        AppUserDto dto = new AppUserDto(appUser.getUsername(), appUser.getRole().name());
        return ResponseEntity.ok(dto);
    }


    @PostMapping("/set-mistress/{id}")
    public ResponseEntity<Void> assignMistress(Authentication authentication, @PathVariable Integer id) {
        MyUser user = (MyUser) authentication.getPrincipal();
        AppUser entity = user.getUser();
        entity.getMistress().setId(id);
        userRepository.save(entity);
        return ResponseEntity.ok().build();
    }
}
