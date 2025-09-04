package services;

import dao.*;
import dto.KittenDto;
import mappers.KittenMapper;
import models.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.ExtensionMethod;
import java.time.LocalDate;
import java.util.ArrayList;

@Setter
@Getter
@AllArgsConstructor
@ExtensionMethod(KittenMapper.class)
public class KittenService implements KittenServiceInterface {
    private final KittenDaoInterface kittenDao;
    private MistressDaoInterface mistressDao;

    @Override
    public KittenDto save(KittenDto kitten) {
        return kittenDao.save(kitten.getKitten()).toDto();
    }

    @Override
    public KittenDto getById(int id) {
        return kittenDao.getById(id).toDto();
    }

    private Breed getBreed(String breed) {
        for(Breed toChoose : Breed.values()) {
            if (toChoose.name().equals(breed)) {
                return toChoose;
            }
        }
        throw new IllegalArgumentException("No such breed");
    }

    private Colour getColour(String color) {
        for(Colour toChoose : Colour.values()) {
            if (toChoose.name().equals(color)) {
                return toChoose;
            }
        }
        throw new IllegalArgumentException("No such color");
    }


    @Override
    public KittenDto create(String name, LocalDate birthDate, String breed, String colour, int mistressId) {
        Mistress mistress = mistressDao.getById(mistressId);
        Kitten kitten = new Kitten(
                name,
                birthDate,
                getBreed(breed),
                getColour(colour),
                null);
        mistress.entangleKitten(kitten);
        kitten.setMistress(mistress);
        mistressDao.update(mistress);
        return kitten.toDto();
    }

    @Override
    public KittenDto createLonelyKitten(String name, LocalDate birthDate, String breed, String colour) {
        Kitten kitten = new Kitten(
                name,
                birthDate,
                getBreed(breed),
                getColour(colour),
                null);
        return kittenDao.save(kitten).toDto();
    }

    @Override
    public void delete(KittenDto kittenDto) {
        Kitten kitten = kittenDao.getById(kittenDto.getId());

        for (Kitten friend : kitten.getFriends()) {
            friend.breakFriendship(kitten);
            kittenDao.update(friend);
        }

        kitten.setFriends(new ArrayList<>());

        Mistress mistress = kitten.getMistress();
        if (mistress != null) {
            mistress.leaveKitten(kitten);
            mistressDao.update(mistress);
            kitten.setMistress(null);
        }

        kittenDao.update(kitten);
        kittenDao.deleteByEntity(kitten);
    }

    @Override
    public void makeFriends(int kitten1Id, int kitten2Id) {
        Kitten kitten1 = kittenDao.getById(kitten1Id);
        Kitten kitten2 = kittenDao.getById(kitten2Id);
        kitten1.makeFriends(kitten2);
        kitten2.makeFriends(kitten1);
        kittenDao.update(kitten1);
        kittenDao.update(kitten2);
    }

    @Override
    public void breakFriendship(int kitten1Id, int kitten2Id) {
        Kitten kitten1 = kittenDao.getById(kitten1Id);
        Kitten kitten2 = kittenDao.getById(kitten2Id);
        kitten1.breakFriendship(kitten2);
        kitten2.breakFriendship(kitten1);
        kittenDao.update(kitten1);
        kittenDao.update(kitten2);
    }

    @Override
    public void changeMistress(int kittenId, int newMistressId) {
        Kitten kitten = kittenDao.getById(kittenId);
        Mistress newMistress = mistressDao.getById(newMistressId);
        Mistress oldMistress = kitten.getMistress();

        if (oldMistress != null && oldMistress.getId() == newMistressId) return;

        if (oldMistress != null) {
            oldMistress.leaveKitten(kitten);
            mistressDao.update(oldMistress);
        }

        newMistress.entangleKitten(kitten);
        kitten.setMistress(newMistress);

        kittenDao.update(kitten);
        mistressDao.update(newMistress);
    }
}
