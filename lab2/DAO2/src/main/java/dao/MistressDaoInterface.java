package dao;

import models.Mistress;

import java.util.List;

public interface MistressDaoInterface {
    Mistress save(Mistress mistress);
    void deleteById(int id);
    void deleteByEntity(Mistress mistress);
    void deleteAll();
    Mistress update(Mistress mistress);
    Mistress getById(int id);
    List<Mistress> getAll();
}
