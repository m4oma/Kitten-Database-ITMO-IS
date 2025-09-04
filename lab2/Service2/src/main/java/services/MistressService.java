package services;

import dao.KittenDaoInterface;
import dao.MistressDaoInterface;
import dto.MistressDto;
import mappers.MistressMapper;
import models.Kitten;
import models.Mistress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.ExtensionMethod;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@ExtensionMethod(MistressMapper.class)
public class MistressService implements MistressServiceInterface {
    private MistressDaoInterface mistressDao;
    private KittenDaoInterface kittenDao;

    @Override
    public MistressDto getById(int id) {
        return mistressDao.getById(id).toDto();
    }

    @Override
    public MistressDto create(String name, LocalDate birthDate) {
        Mistress mistress = new Mistress(name, birthDate);
        mistressDao.save(mistress);
        return mistress.toDto();
    }

    @Override
    public void delete(MistressDto mistress) {
        Mistress mistress1 = mistressDao.getById(mistress.getId());
        for (Kitten kitten : mistress1.getKittens()) {
            kitten.setMistress(null);
            kittenDao.update(kitten);
        }
        mistress1.getKittens().clear();
        mistressDao.update(mistress1);
        mistressDao.deleteByEntity(mistress1);
    }
}
