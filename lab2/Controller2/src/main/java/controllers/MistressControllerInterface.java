package controllers;

import dto.MistressDto;

import java.time.LocalDate;

public interface MistressControllerInterface {
    MistressDto findById(int id);
    MistressDto create(String name, LocalDate birthDate);
    void delete(MistressDto mistress);
}
