import ru.m4oma.dto.MistressDto;
import ru.m4oma.dto.MistressFilter;
import ru.m4oma.models.Kitten;
import ru.m4oma.models.Mistress;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.m4oma.repositories.KittenRepository;
import ru.m4oma.repositories.MistressRepository;
import ru.m4oma.services.MistressService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class MistressServiceTest {
    private MistressRepository mistressDao;
    private KittenRepository kittenDao;
    private MistressService service;

    @BeforeEach
    public void setUp() {
        mistressDao = mock(MistressRepository.class);
        kittenDao = mock(KittenRepository.class);
        service = new MistressService(mistressDao, kittenDao);
    }

    @Test
    public void createMistressTest() {
        String name = "Katya";
        LocalDate birthDate = LocalDate.of(1990, 5, 20);

        MistressDto dto = service.create(name, birthDate);

        ArgumentCaptor<Mistress> captor = ArgumentCaptor.forClass(Mistress.class);
        verify(mistressDao).save(captor.capture());

        Mistress saved = captor.getValue();
        assertEquals(name, saved.getName());
        assertEquals(birthDate, saved.getBirthDate());

        assertEquals(name, dto.getName());
        assertEquals(birthDate, dto.getBirthDate());
        assertNotNull(dto.getKittenIds());
    }

    @Test
    public void getByIdTest() {
        Mistress mistress = new Mistress("Katya", LocalDate.of(1985, 2, 15));
        mistress.setId(1);
        when(mistressDao.getById(1)).thenReturn(mistress);

        MistressDto dto = service.getById(1);

        assertEquals(1, dto.getId());
        assertEquals("Katya", dto.getName());
    }

    @Test
    public void deleteMistressTest() {
        Mistress mistress = new Mistress("Kate", LocalDate.of(1992, 1, 1));
        mistress.setId(10);

        Kitten kitten1 = new Kitten();
        kitten1.setId(1);
        kitten1.setMistress(mistress);

        Kitten kitten2 = new Kitten();
        kitten2.setId(2);
        kitten2.setMistress(mistress);

        List<Kitten> kittens = new ArrayList<>();
        kittens.add(kitten1);
        kittens.add(kitten2);

        mistress.setKittens(kittens);

        when(mistressDao.getById(10)).thenReturn(mistress);

        MistressDto dto = new MistressDto(10, "Kate", mistress.getBirthDate(), List.of(1, 2));
        service.delete(dto);

        verify(kittenDao, times(2)).save(any(Kitten.class));
        verify(mistressDao).delete(mistress);

        assertTrue(mistress.getKittens().isEmpty());
    }

    @Test
    void findAllByNameWithPaginationTest() {
        Mistress m1 = new Mistress("Katya", LocalDate.of(1985, 2, 15));
        Mistress m2 = new Mistress("Kate", LocalDate.of(1985, 2, 15));
        Mistress m3 = new Mistress("Viktoria", LocalDate.of(1985, 2, 15));

        Pageable pageable = PageRequest.of(0, 2);
        List<Mistress> filtered = List.of(m1, m2);
        Page<Mistress> mockPage = new PageImpl<>(filtered, pageable, filtered.size());

        MistressFilter filter = new MistressFilter();
        filter.setName("Kat");

        when(mistressDao.findAll(any(Specification.class), eq(pageable))).thenReturn(mockPage);

        Page<MistressDto> result = service.findAll(filter, pageable);

        assertEquals(2, result.getTotalElements());
        List<String> names = result.getContent().stream().map(MistressDto::getName).toList();
        assertTrue(names.contains("Katya"));
        assertTrue(names.contains("Kate"));
    }
}
