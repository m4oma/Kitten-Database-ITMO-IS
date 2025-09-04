package controllers;

import dto.MistressDto;
import services.MistressServiceInterface;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
public class MistressController implements MistressControllerInterface {
    private final MistressServiceInterface mistressService;

    @Override
    public MistressDto findById(int id) {
        return mistressService.getById(id);
    }

    @Override
    public MistressDto create(String name, LocalDate birthDate) {
        return mistressService.create(name, birthDate);
    }

    @Override
    public void delete(MistressDto mistress) {
        mistressService.delete(mistress);
    }
}
