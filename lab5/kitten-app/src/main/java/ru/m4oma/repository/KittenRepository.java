package ru.m4oma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.m4oma.model.Kitten;

import java.util.List;

@Repository
public interface KittenRepository extends JpaRepository<Kitten, Integer>, JpaSpecificationExecutor<Kitten> {
    List<Kitten> findAllByMistressId(int mistressId );
}
