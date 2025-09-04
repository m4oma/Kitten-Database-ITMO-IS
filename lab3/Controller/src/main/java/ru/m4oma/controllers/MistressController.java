package ru.m4oma.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import ru.m4oma.dto.MistressDto;
import ru.m4oma.dto.MistressFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.m4oma.services.KittenService;
import ru.m4oma.services.MistressService;
import ru.m4oma.dto.*;

import java.time.LocalDate;

@Tag(name = "Mistress Controller", description = "Контроллер хозяек")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mistresses")
public class MistressController {

    private final MistressService mistressService;
    private final KittenService kittenService;

    @Operation(summary = "Получить всех хозяек с фильтрацией и пагинацией")
    @GetMapping
    public ResponseEntity<Page<MistressDto>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDateAfter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDateBefore,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        MistressFilter filter = new MistressFilter();
        filter.setName(name);
        filter.setBirthDateAfter(birthDateAfter);
        filter.setBirthDateBefore(birthDateBefore);

        Pageable pageable = PageRequest.of(page, size);
        Page<MistressDto> result = mistressService.findAll(filter, pageable);

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Хозяйка перекрашивает своего котёнка")
    @PreAuthorize("@kittenAccessChecker.isOwnedByCurrentMistress(#kittenId, authentication) == true ")
    @PatchMapping("/me/kittens/{kittenId}/repaint")
    public ResponseEntity<KittenResponseDto> repaintKitten(
            @PathVariable int kittenId,
            @RequestParam String colour
    ) {
        System.out.println("InController");
        KittenDto updated = kittenService.repaintByMistress(kittenId, colour);
        KittenResponseDto kittenResponseDto = new KittenResponseDto(
                updated.getName(), updated.getBirthDate(),
                updated.getTailLength(), updated.getBreed(),
                updated.getColour(),
                updated.getMistressId());
        return ResponseEntity.ok(kittenResponseDto);
    }

    @Operation(summary = "Получить хозяйку по ID")
    @GetMapping("/{id}")
    public ResponseEntity<MistressDto> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(mistressService.getById(id));
    }

    @Operation(summary = "Создать новую хозяйку")
    @PostMapping
    public ResponseEntity<MistressDto> create(@Valid @RequestBody MistressDto dto) {
        return ResponseEntity.ok(mistressService.create(dto.getName(), dto.getBirthDate()));
    }

    @Operation(summary = "Удалить котёнка, если он принадлежит авторизованной хозяйке")
    @PreAuthorize("@kittenAccessChecker.isOwnedByCurrentMistress(#kittenId, authentication)")
    @DeleteMapping("/kittens/{kittenId}")
    public ResponseEntity<Void> deleteKitten(@PathVariable int kittenId, Authentication auth) {
        kittenService.deleteByMistress(kittenId, auth);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Удалить хозяйку по ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        mistressService.delete(mistressService.getById(id));
        return ResponseEntity.noContent().build();
    }
}