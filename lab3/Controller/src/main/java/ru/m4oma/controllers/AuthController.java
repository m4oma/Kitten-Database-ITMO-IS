package ru.m4oma.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.m4oma.dto.UserDto;
import ru.m4oma.security.AuthService;
import ru.m4oma.security.MyUser;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(authService.register(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(authService.login(userDto));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> currentUser(@AuthenticationPrincipal MyUser user) {
        return ResponseEntity.ok(authService.getCurrentUser(user));
    }

}
