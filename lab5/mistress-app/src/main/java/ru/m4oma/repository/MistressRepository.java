package ru.m4oma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.m4oma.model.Mistress;

@Repository
public interface MistressRepository extends JpaRepository<Mistress, Integer>, JpaSpecificationExecutor<Mistress> {
}