package ru.m4oma.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.m4oma.dto.MistressDto;
import ru.m4oma.dto.MistressFilter;
import ru.m4oma.dto.MistressMapper;
import ru.m4oma.model.Mistress;
import ru.m4oma.repository.MistressRepository;
import ru.m4oma.repository.MistressSpecifications;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
@ExtensionMethod(MistressMapper.class)
public class MistressService {

    private final MistressRepository mistressRepository;

    @Transactional(readOnly = true)
    public MistressDto getById(int id) {
        Mistress mistress = mistressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mistress not found"));
        return mistress.toDto();
    }

    @Transactional
    public MistressDto create(String name, LocalDate birthDate) {
        Mistress mistress = new Mistress(name, birthDate);
        return mistressRepository.save(mistress).toDto();
    }

    @Transactional
    public void delete(MistressDto dto) {
        Mistress mistress = mistressRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Mistress not found"));
        mistressRepository.delete(mistress);
    }


    @Transactional(readOnly = true)
    public Page<MistressDto> findAll(MistressFilter filter, Pageable pageable) {
        var spec = MistressSpecifications.getSpecification(
                filter.getName(), filter.getBirthDateAfter(), filter.getBirthDateBefore()
        );
        return mistressRepository.findAll(spec, pageable).map(MistressMapper::toDto);
    }
}
