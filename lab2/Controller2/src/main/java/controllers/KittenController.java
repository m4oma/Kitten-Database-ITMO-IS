package controllers;

import dto.KittenDto;
import services.*;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
public class KittenController implements KittenControllerInterface {
    private final KittenServiceInterface kittenService;

    @Override
    public KittenDto findById(int id) {
        return kittenService.getById(id);
    }

    @Override
    public KittenDto create(String name, LocalDate localDate, String breed, String color, int ownerId) {
        return kittenService.create(name, localDate, breed, color, ownerId);
    }

    @Override
    public KittenDto createLonelyKitten(String name, LocalDate birthDate, String breed, String colour) {
        return kittenService.createLonelyKitten(name, birthDate, breed, colour);
    }

    @Override
    public void delete(KittenDto kitten) {
        kittenService.delete(kitten);
    }

    @Override
    public void makeFriends(int kitten1Id, int kitten2Id) {
        kittenService.makeFriends(kitten1Id, kitten2Id);
    }

    @Override
    public void breakFriendship(int kitten1Id, int kitten2Id) {
        kittenService.breakFriendship(kitten1Id, kitten2Id);
    }

    @Override
    public void changeMistress(int kittenId, int newMistressId) {
        kittenService.changeMistress(kittenId, newMistressId);
    }
}
