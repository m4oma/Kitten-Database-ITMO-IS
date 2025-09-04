import dao.KittenDaoInterface;
import dao.MistressDaoInterface;
import dto.MistressDto;
import lombok.experimental.ExtensionMethod;
import mappers.MistressMapper;
import models.Kitten;
import models.Mistress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.MistressService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtensionMethod(MistressMapper.class)
class MistressServiceTest {
    private MistressDaoInterface mistressDao;
    private KittenDaoInterface kittenDao;
    private MistressService mistressService;

    @BeforeEach
    void setup() {
        mistressDao = mock(MistressDaoInterface.class);
        kittenDao = mock(KittenDaoInterface.class);
        mistressService = new MistressService(mistressDao, kittenDao);
    }

    @Test
    void createMistressTest() {
        Mistress mistress = new Mistress("Viktoria", LocalDate.of(1980, 1, 1));
        mistress.setId(10);
        when(mistressDao.save(any(Mistress.class))).thenReturn(mistress);
        MistressDto result = mistressService.create("Viktoria", LocalDate.of(1980, 1, 1));

        assertEquals("Viktoria", result.getName());
    }

    @Test
    void deleteMistressTest() {
        Mistress mistress = new Mistress("Viktoria", LocalDate.of(1991, 9, 9));
        mistress.setId(99);
        Kitten kitten1 = new Kitten("Yaroslav", LocalDate.now(), null, null, mistress);
        Kitten kitten2 = new Kitten("Slava", LocalDate.now(), null, null, mistress);
        mistress.setKittens(new ArrayList<>(List.of(kitten1, kitten2)));
        MistressDto dto = mistress.toDto();
        when(mistressDao.getById(99)).thenReturn(mistress);
        mistressService.delete(dto);

        verify(kittenDao, times(2)).update(any(Kitten.class));
        verify(mistressDao).update(mistress);
        verify(mistressDao).deleteByEntity(mistress);
    }
}
