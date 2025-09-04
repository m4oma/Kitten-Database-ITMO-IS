import ru.m4oma.dto.KittenDto;
import ru.m4oma.dto.KittenFilter;
import ru.m4oma.models.Breed;
import ru.m4oma.models.Colour;
import ru.m4oma.models.Kitten;
import ru.m4oma.models.Mistress;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.m4oma.repositories.KittenRepository;
import ru.m4oma.repositories.MistressRepository;
import ru.m4oma.services.KittenService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class KittenServiceTest {
    private KittenRepository kittenDao;
    private MistressRepository mistressDao;
    private KittenService service;

    @BeforeEach
    public void setUp() {
        kittenDao = mock(KittenRepository.class);
        mistressDao = mock(MistressRepository.class);
        service = new KittenService(kittenDao, mistressDao);
    }

    @Test
    public void saveKittenTest() {
        Kitten kitten = new Kitten("Yaroslav", LocalDate.now(), 12.5, Breed.BENGAL, Colour.BLACK, new Mistress("Katya", LocalDate.of(1985, 2, 15)));
        kitten.setId(1);
        when(kittenDao.save(any())).thenReturn(kitten);

        KittenDto dto = new KittenDto(1, "Yaroslav", kitten.getBirthDate(), 12.5, "BIRMAN", "BLACK", 0, new ArrayList<>());
        KittenDto saved = service.save(dto);

        assertEquals("Yaroslav", saved.getName());
        assertEquals("BLACK", saved.getColour());
    }

    @Test
    public void getByIdTest() {
        Mistress mistress = new Mistress("Katya", LocalDate.of(1985, 2, 15));
        mistress.setId(1);

        Kitten kitten = new Kitten("Yaroslav", LocalDate.of(2020, 3, 1), 14.0, Breed.BRITISH, Colour.WHITE, mistress);
        kitten.setId(2);
        kitten.setFriends(new ArrayList<>());

        when(kittenDao.findById(2)).thenReturn(Optional.of(kitten));

        KittenDto dto = service.getById(2);

        assertNotNull(dto);
        assertEquals("Yaroslav", dto.getName());
        assertEquals("WHITE", dto.getColour());
        assertEquals(1, dto.getMistressId());
        assertEquals(0, dto.getFriendIds().size());
    }

    @Test
    public void createKittenTest() {
        Mistress mistress = new Mistress("Kate", LocalDate.of(1990, 1, 1));
        mistress.setId(3);

        Kitten kitten = new Kitten("Yaroslav", LocalDate.now(), 11.1, Breed.BIRMAN, Colour.WHITE, mistress);
        kitten.setId(10);

        KittenDto inputDto = new KittenDto(
                1,
                "Yaroslav",
                kitten.getBirthDate(),
                11.1,
                "BIRMAN",
                "WHITE",
                3,
                new ArrayList<>()
        );

        when(mistressDao.findById(3)).thenReturn(Optional.of(mistress));
        when(kittenDao.save(any(Kitten.class))).thenReturn(kitten);

        KittenDto result = service.create(inputDto);
        assertEquals("Yaroslav", result.getName());
        assertEquals("WHITE", result.getColour());
        assertEquals(11.1, result.getTailLength());
    }

    @Test
    public void createLonelyKittenTest() {
        Kitten kitten = new Kitten("Yaroslav", LocalDate.of(2021, 5, 10), 11.1, Breed.BENGAL, Colour.WHITE, null);
        kitten.setId(7);
        when(kittenDao.save(any())).thenReturn(kitten);

        KittenDto dto = service.createLonelyKitten("Yaroslav", kitten.getBirthDate(), 10.1, "BENGAL", "WHITE");
        assertEquals("Yaroslav", dto.getName());
        assertEquals("WHITE", dto.getColour());
    }

    @Test
    public void deleteKittenTest() {
        Mistress mistress = new Mistress("Katya", LocalDate.of(1985, 1, 1));
        mistress.setId(5);

        Kitten kitten = new Kitten("Yaroslav", LocalDate.of(2020, 4, 1), 10.2, Breed.BRITISH, Colour.GRAY, mistress);
        kitten.setId(10);
        kitten.setFriends(new ArrayList<>());

        when(kittenDao.findById(10)).thenReturn(Optional.of(kitten));

        KittenDto dto = new KittenDto(10, "Yaroslav", kitten.getBirthDate(), 11.1, "BIRMAN", "GRAY", 5, new ArrayList<>());
        service.delete(dto);

        verify(mistressDao).save(mistress);
        verify(kittenDao).delete(kitten);
    }

    @Test
    public void makeFriendsTest() {
        Kitten k1 = new Kitten("Yaroslav", LocalDate.now(), 8.8, Breed.BENGAL, Colour.BLACK, null);
        k1.setId(1);
        Kitten k2 = new Kitten("Denisik", LocalDate.now(), 8.8, Breed.BRITISH, Colour.GRAY, null);
        k2.setId(2);

        when(kittenDao.findById(1)).thenReturn(Optional.of(k1));
        when(kittenDao.findById(2)).thenReturn(Optional.of(k2));

        service.makeFriends(1, 2);

        verify(kittenDao, times(1)).save(k1);
        verify(kittenDao, times(1)).save(k2);
        assertTrue(k1.getFriends().contains(k2));
        assertTrue(k2.getFriends().contains(k1));
    }

    @Test
    public void breakFriendsTest() {
        Kitten k1 = new Kitten("Yaroslav", LocalDate.now(), 5.2, Breed.BRITISH, Colour.BLACK, null);
        k1.setId(1);
        Kitten k2 = new Kitten("Denisik", LocalDate.now(), 8.8, Breed.BENGAL, Colour.WHITE, null);
        k2.setId(2);

        k1.makeFriends(k2);
        k2.makeFriends(k1);

        when(kittenDao.findById(1)).thenReturn(Optional.of(k1));
        when(kittenDao.findById(2)).thenReturn(Optional.of(k2));

        service.breakFriendship(1, 2);

        verify(kittenDao, times(1)).save(k1);
        verify(kittenDao, times(1)).save(k2);
        assertFalse(k1.getFriends().contains(k2));
        assertFalse(k2.getFriends().contains(k1));
    }

    @Test
    public void changeMistressTest() {
        Mistress oldMistress = new Mistress("Старая", LocalDate.of(1980, 1, 1));
        oldMistress.setId(1);
        Mistress newMistress = new Mistress("Новая", LocalDate.of(2006, 1, 1));
        newMistress.setId(2);

        Kitten kitten = new Kitten("Yaroslav", LocalDate.now(), 6.6, Breed.BRITISH, Colour.BLACK, oldMistress);
        kitten.setId(3);

        when(kittenDao.findById(3)).thenReturn(Optional.of(kitten));
        when(mistressDao.findById(2)).thenReturn(Optional.of(newMistress));

        service.changeMistress(3, 2);

        verify(mistressDao).save(oldMistress);
        verify(mistressDao).save(newMistress);
        verify(kittenDao).save(kitten);

        assertEquals(newMistress, kitten.getMistress());
    }

    @Test
    void findAllByColourAndTailLengthTest() {
        Mistress mistress = new Mistress("Katya", LocalDate.now());

        Kitten k1 = new Kitten("Yaroslav", LocalDate.now().minusYears(1), 15.0, Breed.BENGAL, Colour.ORANGE, mistress);
        Kitten k2 = new Kitten("Denisik", LocalDate.now().minusYears(2), 20.0, Breed.BRITISH, Colour.BLACK, mistress);

        Pageable pageable = PageRequest.of(0, 2);
        List<Kitten> filtered = List.of(k1);
        Page<Kitten> mockPage = new PageImpl<>(filtered, pageable, filtered.size());

        KittenFilter filter = new KittenFilter();
        filter.setColour("ORANGE");
        filter.setTailLengthMin(10.0);
        filter.setTailLengthMax(18.0);

        when(kittenDao.findAll(any(Specification.class), eq(pageable))).thenReturn(mockPage);

        Page<KittenDto> result = service.findAll(filter, pageable);

        assertEquals(1, result.getTotalElements());
        List<String> names = result.getContent().stream().map(KittenDto::getName).toList();
        assertTrue(names.contains("Yaroslav"));
    }
}
