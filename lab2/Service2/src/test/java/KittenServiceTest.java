import dao.KittenDaoInterface;
import dao.MistressDaoInterface;
import dto.KittenDto;
import lombok.experimental.ExtensionMethod;
import mappers.KittenMapper;
import models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.KittenService;

import java.time.LocalDate;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtensionMethod(KittenMapper.class)
class KittenServiceTest {
    private KittenDaoInterface kittenDao;
    private MistressDaoInterface mistressDao;
    private KittenService kittenService;

    @BeforeEach
    void setUp() {
        kittenDao = mock(KittenDaoInterface.class);
        mistressDao = mock(MistressDaoInterface.class);
        kittenService = new KittenService(kittenDao, mistressDao);
    }

    @Test
    void createKittenWithMistressTest() {
        Mistress mistress = new Mistress("Viktoria", LocalDate.of(1990, 1, 1));
        mistress.setId(1);
        when(mistressDao.getById(1)).thenReturn(mistress);
        Kitten saved = new Kitten("Yaroslav", LocalDate.of(2020, 5, 5), Breed.BIRMAN, Colour.BLACK, mistress);
        saved.setId(10);
        when(kittenDao.save(any(Kitten.class))).thenReturn(saved);
        KittenDto result = kittenService.create("Viktoria", LocalDate.of(2020, 5, 5), "BIRMAN", "BLACK", 1);

        assertEquals("Viktoria", result.getName());
        assertEquals("BLACK", result.getColour());
        assertEquals(1, result.getMistressId());
    }

    @Test
    void deleteKittenTest() {
        Mistress mistress = new Mistress("Viktoria", LocalDate.of(1990, 1, 1));
        mistress.setId(1);
        Kitten kitten = new Kitten("Yaroslav", LocalDate.of(2020, 5, 5), Breed.SIAMESE, Colour.WHITE, mistress);
        kitten.setId(42);
        mistress.getKittens().add(kitten);
        KittenDto dto = kitten.toDto();
        when(kittenDao.getById(42)).thenReturn(kitten);
        when(mistressDao.getById(42)).thenReturn(mistress);
        kittenService.delete(dto);

        verify(kittenDao).deleteByEntity(kitten);
        verify(kittenDao).update(kitten);
        verify(mistressDao).update(mistress);
    }

    @Test
    void changeMistressTest() {
        Mistress oldMistress = new Mistress("Viktoria", LocalDate.of(1985, 4, 20));
        Mistress newMistress = new Mistress("Alya", LocalDate.of(1995, 7, 12));
        oldMistress.setId(1);
        newMistress.setId(2);
        Kitten kitten = new Kitten("Yaroslav", LocalDate.of(2021, 3, 15), Breed.BENGAL, Colour.BLACK, oldMistress);
        kitten.setId(5);
        oldMistress.getKittens().add(kitten);
        when(mistressDao.getById(2)).thenReturn(newMistress);
        when(kittenDao.getById(5)).thenReturn(kitten);
        kittenService.changeMistress(5, 2);

        assertEquals(newMistress, kitten.getMistress());
        verify(mistressDao).update(oldMistress);
        verify(mistressDao).update(newMistress);
        verify(kittenDao).update(kitten);
    }
}
