package ru.m4oma.service;


import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.m4oma.dto.KittenDto;
import ru.m4oma.dto.KittenFilter;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.m4oma.dto.KittenMapper;
import ru.m4oma.model.*;
import ru.m4oma.repository.KittenRepository;
import ru.m4oma.repository.KittenSpecifications;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Transactional
public class KittenService {
    private final KittenRepository kittenRepository;

    private static Breed getBreed(String breed) {
        try {
            return Breed.valueOf(breed.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid breed: " + breed);
        }
    }

    private static Colour getColour(String colour) {
        try {
            return Colour.valueOf(colour.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid colour: " + colour);
        }
    }

    public KittenDto save(KittenDto dto) {
        if (dto.getTailLength() < 0) {
            throw new IllegalArgumentException("Tail length must be >= 0");
        }

        List<Kitten> friends = dto.getFriendIds() == null
                ? List.of()
                : kittenRepository.findAllById(dto.getFriendIds());

        Kitten kitten = KittenMapper.fromDto(dto, friends);
        return KittenMapper.toDto(kittenRepository.save(kitten));
    }

    public KittenDto getById(int id) {
        Kitten kitten = kittenRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kitten not found"));
        return KittenMapper.toDto(kitten);
    }

    public void deleteById(int id) {
        Kitten kitten = kittenRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kitten not found"));

        for (Kitten friend : kitten.getFriends()) {
            friend.breakFriendship(kitten);
            kittenRepository.save(friend);
        }

        kittenRepository.delete(kitten);
    }

    public void makeFriends(int id1, int id2) {
        if (id1 == id2) return;

        Kitten k1 = kittenRepository.findById(id1).orElseThrow();
        Kitten k2 = kittenRepository.findById(id2).orElseThrow();

        k1.makeFriends(k2);
        k2.makeFriends(k1);

        kittenRepository.saveAll(List.of(k1, k2));
    }

    public void breakFriendship(int id1, int id2) {
        Kitten k1 = kittenRepository.findById(id1).orElseThrow();
        Kitten k2 = kittenRepository.findById(id2).orElseThrow();

        k1.breakFriendship(k2);
        k2.breakFriendship(k1);

        kittenRepository.saveAll(List.of(k1, k2));
    }

    @Transactional
    public KittenDto repaint(int kittenId, String newColour) {
        Kitten kitten = kittenRepository.findById(kittenId)
                .orElseThrow(() -> new EntityNotFoundException("Kitten not found"));
        kitten.setColour(getColour(newColour));
        return KittenMapper.toDto(kittenRepository.save(kitten));
    }

    public void changeMistress(int kittenId, Integer newMistressId) {
        Kitten kitten = kittenRepository.findById(kittenId)
                .orElseThrow(() -> new EntityNotFoundException("Kitten not found"));

        if (Objects.equals(kitten.getMistressId(), newMistressId)) return;

        kitten.setMistressId(newMistressId);
        kittenRepository.save(kitten);
    }

    public Page<KittenDto> findAll(KittenFilter filter, Pageable pageable) {
        Specification<Kitten> spec = KittenSpecifications.getSpecification(
                filter.getName(), filter.getBreed(), filter.getColour(),
                filter.getTailLengthMin(), filter.getTailLengthMax(), filter.getMistressId()
        );
        return kittenRepository.findAll(spec, pageable)
                .map(KittenMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<KittenDto> findByMistressId(int mistressId) {
        return kittenRepository.findAllByMistressId(mistressId).stream()
                .map(KittenMapper::toDto)
                .collect(Collectors.toList());
    }

}
