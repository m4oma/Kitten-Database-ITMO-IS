package controllers;

import dto.KittenDto;

import java.time.LocalDate;

public interface KittenControllerInterface {
    KittenDto findById(int id);
    KittenDto create(String name, LocalDate localDate, String breed, String color, int ownerId);
    KittenDto createLonelyKitten(String name, LocalDate birthDate, String breed, String colour);
    void delete(KittenDto kitten);
    void makeFriends(int kitten1Id, int kitten2Id);
    void breakFriendship(int kitten1Id, int kitten2Id);
    void changeMistress(int kittenId, int newMistressId);
}
