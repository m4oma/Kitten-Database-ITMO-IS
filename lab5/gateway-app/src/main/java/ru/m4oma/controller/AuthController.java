package ru.m4oma.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.m4oma.dto.request.FullUserInfo;
import ru.m4oma.dto.user.LoginRequestDto;
import ru.m4oma.dto.user.RegisterUserRequestDto;
import ru.m4oma.dto.user.TokenResponseDto;
import ru.m4oma.dto.user.UserResponseDto;
import ru.m4oma.security.MyUserDetails;
import ru.m4oma.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody RegisterUserRequestDto dto) {
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> currentUser(@AuthenticationPrincipal MyUserDetails user) {
        return ResponseEntity.ok(authService.getCurrentUser(user));
    }

    @GetMapping("/me/full")
    public ResponseEntity<FullUserInfo> getCurrentUserFull(@AuthenticationPrincipal MyUserDetails userDetails) {
        return ResponseEntity.ok(authService.getCurrentUserFull(userDetails));
    }
}
