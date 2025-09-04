package services;

import dto.MistressDto;

import java.time.LocalDate;

public interface MistressServiceInterface {
    MistressDto getById(int id);
    MistressDto create(String name, LocalDate birthDate);
    void delete(MistressDto mistressDto);
}
