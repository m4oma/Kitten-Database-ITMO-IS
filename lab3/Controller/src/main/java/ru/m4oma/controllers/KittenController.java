package ru.m4oma.controllers;

import ru.m4oma.dto.KittenDto;
import ru.m4oma.dto.KittenFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.m4oma.services.KittenService;

@Tag(name = "Kitten Controller", description = "Управление котятами")
@RestController
@RequestMapping("/api/kittens")
@RequiredArgsConstructor
public class KittenController {

    private final KittenService kittenService;

    @Operation(summary = "Получить всех котят с фильтрацией и пагинацией", description = "meow")
    @GetMapping
    public ResponseEntity<Page<KittenDto>> getAll(
            @RequestParam(required = false) String colour,
            @RequestParam(required = false) Double tailLengthMin,
            @RequestParam(required = false) Double tailLengthMax,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        KittenFilter filter = new KittenFilter();
        filter.setColour(colour);
        filter.setTailLengthMin(tailLengthMin);
        filter.setTailLengthMax(tailLengthMax);

        Pageable pageable = PageRequest.of(page, size);
        Page<KittenDto> result = kittenService.findAll(filter, pageable);

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Создает котенка")
    @PostMapping
    public ResponseEntity<KittenDto> create(@Valid @RequestBody KittenDto dto) {
        return ResponseEntity.ok(kittenService.create(dto));
    }

    @Operation(summary = "Находит котенка по id")
    @GetMapping("/{id}")
    public ResponseEntity<KittenDto> findById(@PathVariable int id) {
        KittenDto dto = kittenService.getById(id);
        return ResponseEntity.ok(dto);
    }
}
