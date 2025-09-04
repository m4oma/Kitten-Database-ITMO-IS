package ru.m4oma.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.m4oma.communication.KittenClient;
import ru.m4oma.communication.MistressClient;
import ru.m4oma.dto.KittenDto;
import ru.m4oma.dto.MistressDto;
import ru.m4oma.dto.request.CreateKittenRequest;
import ru.m4oma.dto.request.CreateMistressRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final MistressClient mistressClient;
    private final KittenClient kittenClient;

    @PostMapping("/create-mistress")
    public ResponseEntity<MistressDto> createMistress(@RequestBody CreateMistressRequest request) {

        return mistressClient.createMistress(request)
                .map(ResponseEntity::ok)
                .orElseThrow();
    }

    @PostMapping("/create-kitten")
    public ResponseEntity<KittenDto> createKitten(@RequestBody CreateKittenRequest request) {
        return kittenClient.createKitten(request)
                .map(ResponseEntity::ok).orElseThrow();
    }

    @PreAuthorize("@kittenAccessChecker.hasAccessToKitten(#kittenId, authentication)")
    @PatchMapping("/kittens/{kittenId}/repaint")
    public ResponseEntity<KittenDto> repaintKitten(
            @PathVariable int kittenId,
            @RequestParam String newColor
    ) {
        return kittenClient.repaintKitten(kittenId, newColor)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Repaint failed"));
    }
}
