package dao;

import models.*;

import java.util.List;

public interface KittenDaoInterface {
    Kitten save(Kitten kitten);
    void deleteById(int id);
    void deleteByEntity(Kitten kitten);
    void deleteAll();
    Kitten update(Kitten kitten);
    Kitten getById(int id);
    List<Kitten> getAll();
}
