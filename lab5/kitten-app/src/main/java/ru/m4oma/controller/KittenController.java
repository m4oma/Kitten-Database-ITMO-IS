package ru.m4oma.controller;

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
import ru.m4oma.service.KittenService;

@RestController
@RequestMapping("/api/kittens")
@RequiredArgsConstructor
public class KittenController {

    private final KittenService kittenService;

    @Operation(summary = "Получить всех котят с фильтрацией и пагинацией")
    @GetMapping
    public ResponseEntity<Page<KittenDto>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) String colour,
            @RequestParam(required = false) Double tailLengthMin,
            @RequestParam(required = false) Double tailLengthMax,
            @RequestParam(required = false) Integer mistressId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        KittenFilter filter = new KittenFilter();
        filter.setName(name);
        filter.setBreed(breed);
        filter.setColour(colour);
        filter.setTailLengthMin(tailLengthMin);
        filter.setTailLengthMax(tailLengthMax);
        filter.setMistressId(mistressId);

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(kittenService.findAll(filter, pageable));
    }

    @Operation(summary = "Создать или обновить котёнка")
    @PostMapping
    public ResponseEntity<KittenDto> save(@Valid @RequestBody KittenDto dto) {
        return ResponseEntity.ok(kittenService.save(dto));
    }

    @Operation(summary = "Получить котёнка по ID")
    @GetMapping("/{id}")
    public ResponseEntity<KittenDto> getById(@PathVariable int id) {
        return ResponseEntity.ok(kittenService.getById(id));
    }

    @Operation(summary = "Удалить котёнка по ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        kittenService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Подружить двух котят")
    @PostMapping("/{id}/friends")
    public ResponseEntity<Void> makeFriends(
            @PathVariable int id,
            @RequestParam int friendId
    ) {
        kittenService.makeFriends(id, friendId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Разорвать дружбу между котятами")
    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> breakFriendship(
            @PathVariable int id,
            @PathVariable int friendId
    ) {
        kittenService.breakFriendship(id, friendId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Сменить хозяйку котёнку")
    @PatchMapping("/{id}/mistress/{newMistressId}")
    public ResponseEntity<Void> changeMistress(
            @PathVariable int id,
            @PathVariable Integer newMistressId
    ) {
        kittenService.changeMistress(id, newMistressId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Перекрасить котёнка")
    @PatchMapping("/{id}/repaint")
    public ResponseEntity<KittenDto> repaint(
            @PathVariable int id,
            @RequestParam String newColour
    ) {
        return ResponseEntity.ok(kittenService.repaint(id, newColour));
    }
}
