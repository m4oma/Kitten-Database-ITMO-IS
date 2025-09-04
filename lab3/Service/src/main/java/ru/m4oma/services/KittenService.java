package ru.m4oma.services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import ru.m4oma.dto.KittenDto;
import ru.m4oma.dto.KittenFilter;
import jakarta.persistence.EntityNotFoundException;
import lombok.experimental.ExtensionMethod;
import ru.m4oma.mappers.KittenMapper;
import ru.m4oma.models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.m4oma.repositories.KittenRepository;
import ru.m4oma.repositories.MistressRepository;
import ru.m4oma.repositories.specifications.KittenSpecifications;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Transactional
@ExtensionMethod(KittenMapper.class)
@Service
@AllArgsConstructor
public class KittenService {
    private final KittenRepository kittenRepository;
    private final MistressRepository mistressRepository;


    private Breed getBreed(String breed) {
        try {
            return Breed.valueOf(breed.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid breed: " + breed);
        }
    }

    private Colour getColour(String colour) {
        try {
            return Colour.valueOf(colour.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid colour: " + colour);
        }
    }

    @Transactional
    public KittenDto save(KittenDto dto) {
        if (dto.getTailLength() < 0) {
            throw new RuntimeException("Invalid tail length: " + dto.getTailLength());
        }
        Mistress mistress = mistressRepository.findById(dto.getMistressId()).orElse(null);
        List<Kitten> friends = dto.getFriendIds() == null
                ? new ArrayList<>()
                : kittenRepository.findAllById(dto.getFriendIds());
        Kitten kitten = KittenMapper.fromDto(dto, mistress, friends);
        return KittenMapper.toDto(kittenRepository.save(kitten));
    }

    @Transactional
    public KittenDto getById(int id) {
        Kitten kitten = kittenRepository.findById(id).orElseThrow(() -> new RuntimeException("Mistress not found"));
        return KittenMapper.toDto(kitten);
    }


    @Transactional
    public KittenDto create(KittenDto dto) {
        if (dto.getTailLength() < 0) {
            throw new RuntimeException("Invalid tail length: " + dto.getTailLength());
        }
        Mistress mistress = mistressRepository.findById(dto.getMistressId()).orElseThrow(() -> new EntityNotFoundException("Mistress not found"));
        Kitten kitten = new Kitten(dto.getName(), dto.getBirthDate(), dto.getTailLength(), getBreed(dto.getBreed()), getColour(dto.getColour()), null);
        mistress.entangleKitten(kitten);
        mistressRepository.save(mistress);
        return kitten.toDto();
    }

    @Transactional
    public KittenDto createLonelyKitten(String name, LocalDate birthDate, double tailLength, String breed, String colour) {
        if (tailLength < 0) {
            throw new RuntimeException("Invalid tail length: " + tailLength);
        }
        Kitten kitten = new Kitten(name, birthDate, tailLength, getBreed(breed), getColour(colour), null);
        return KittenMapper.toDto(kittenRepository.save(kitten));
    }

    @Transactional
    public void delete(KittenDto dto) {
        Kitten kitten = kittenRepository.findById(dto.getId()).orElseThrow();
        for (Kitten friend : kitten.getFriends()) {
            friend.breakFriendship(kitten);
            kittenRepository.save(friend);
        }

        kitten.setFriends(new ArrayList<>());
        if (kitten.getMistress() != null) {
            Mistress mistress = kitten.getMistress();
            mistress.leaveKitten(kitten);
            mistressRepository.save(mistress);
            kitten.setMistress(null);
        }

        kittenRepository.save(kitten);
        kittenRepository.delete(kitten);
    }

    public void deleteByMistress(int kittenId, Authentication authentication) {
        Kitten kitten = kittenRepository.findById(kittenId)
                .orElseThrow(() -> new EntityNotFoundException("Котёнок не найден"));

        kittenRepository.delete(kitten);
    }


    @Transactional
    public void makeFriends(int kitten1Id, int kitten2Id) {
        Kitten kitten1 = kittenRepository.findById(kitten1Id).orElseThrow();
        Kitten kitten2 = kittenRepository.findById(kitten2Id).orElseThrow();
        kitten1.makeFriends(kitten2);
        kitten2.makeFriends(kitten1);
        kittenRepository.save(kitten1);
        kittenRepository.save(kitten2);
    }

    @Transactional
    public void breakFriendship(int kitten1Id, int kitten2Id) {
        Kitten kitten1 = kittenRepository.findById(kitten1Id).orElseThrow();
        Kitten kitten2 = kittenRepository.findById(kitten2Id).orElseThrow();
        kitten1.breakFriendship(kitten2);
        kitten2.breakFriendship(kitten1);
        kittenRepository.save(kitten1);
        kittenRepository.save(kitten2);
    }

    @Transactional
    public void changeMistress(int kittenId, int newMistressId) {
        Kitten kitten = kittenRepository.findById(kittenId).orElseThrow();
        Mistress newMistress = mistressRepository.findById(newMistressId).orElseThrow();
        Mistress oldMistress = kitten.getMistress();

        if (oldMistress != null && oldMistress.getId() == newMistressId) return;

        if (oldMistress != null) {
            oldMistress.leaveKitten(kitten);
            mistressRepository.save(oldMistress);
        }

        newMistress.entangleKitten(kitten);
        kitten.setMistress(newMistress);

        kittenRepository.save(kitten);
        mistressRepository.save(newMistress);
    }

    public KittenDto repaintByMistress(int kittenId, String newColour) {
        System.out.println("In repaint service method");
        Kitten kitten = kittenRepository.findById(kittenId)
                .orElseThrow(() -> new EntityNotFoundException("Котёнок не найден"));
        kitten.setColour(getColour(newColour));
        kittenRepository.save(kitten);
        return KittenMapper.toDto(kitten);
    }


    public Page<KittenDto> findAll(KittenFilter filter, Pageable pageable) {
        var spec = KittenSpecifications.getSpecification(
                filter.getName(), filter.getBreed(), filter.getColour(), filter.getTailLengthMin(), filter.getTailLengthMax(), filter.getMistressId()
        );
        Page<Kitten> page = kittenRepository.findAll(spec, pageable);
        return page.map(KittenMapper::toDto);
    }
}
