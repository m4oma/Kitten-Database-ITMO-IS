package ru.m4oma.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.m4oma.models.AppUser;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    Optional<AppUser> findByUsername(String username);
}
