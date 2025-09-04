package ru.m4oma.services;

import ru.m4oma.dto.MistressDto;
import ru.m4oma.dto.MistressFilter;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.ExtensionMethod;
import ru.m4oma.mappers.MistressMapper;
import ru.m4oma.models.Mistress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.m4oma.repositories.KittenRepository;
import ru.m4oma.repositories.MistressRepository;
import ru.m4oma.repositories.specifications.MistressSpecifications;

import java.time.LocalDate;

@Service
@Transactional
@Setter
@Getter
@AllArgsConstructor
@ExtensionMethod(MistressMapper.class)
public class MistressService {
    private MistressRepository mistressRepository;
    private KittenRepository kittenRepository;


    @Transactional
    public MistressDto getById(int id) {
        return mistressRepository.getById(id).toDto();
    }

    @Transactional
    public MistressDto create(String name, LocalDate birthDate) {
        Mistress mistress = new Mistress(name, birthDate);
        mistressRepository.save(mistress);
        return mistress.toDto();
    }
    @Transactional
    public void delete(MistressDto mistressDto) {
        Mistress mistress = mistressRepository.getById(mistressDto.getId());

        mistress.getKittens().forEach(kitten -> {
            kitten.setMistress(null);
            kittenRepository.save(kitten);
        });

        mistress.getKittens().clear();
        mistressRepository.save(mistress);

        mistressRepository.delete(mistress);
    }

    public Page<MistressDto> findAll(MistressFilter filter, Pageable pageable) {
        var spec = MistressSpecifications.getSpecification(
                filter.getName(), filter.getBirthDateAfter(), filter.getBirthDateBefore()
        );

        Page<Mistress> page = mistressRepository.findAll(spec, pageable);
        return page.map(MistressMapper::toDto);
    }

}
