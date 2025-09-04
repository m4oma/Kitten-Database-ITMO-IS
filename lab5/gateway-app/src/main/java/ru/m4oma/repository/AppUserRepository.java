package ru.m4oma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.m4oma.model.AppUser;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    Optional<AppUser> findByUsername(String username);
}

