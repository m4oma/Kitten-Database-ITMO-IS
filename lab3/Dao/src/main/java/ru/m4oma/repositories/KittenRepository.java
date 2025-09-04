package ru.m4oma.repositories;

import ru.m4oma.models.Kitten;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface KittenRepository extends JpaRepository<Kitten, Integer>, JpaSpecificationExecutor<Kitten> {
}