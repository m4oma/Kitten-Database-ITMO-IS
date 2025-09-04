package ru.m4oma.repositories;

import ru.m4oma.models.Mistress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MistressRepository extends JpaRepository<Mistress, Integer>, JpaSpecificationExecutor<Mistress> {
}