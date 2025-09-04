package ru.m4oma.controllers;

import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.m4oma.models.AppUser;
import ru.m4oma.models.Mistress;
import ru.m4oma.models.Role;
import ru.m4oma.repositories.AppUserRepository;
import ru.m4oma.repositories.MistressRepository;

import java.time.LocalDate;

@Configuration
@AllArgsConstructor
public class AdminInitializer {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final MistressRepository mistressRepository;

    @Bean
    public ApplicationRunner initAdmin() {
        return args -> {
            if (appUserRepository.findByUsername("admin").isEmpty()) {
                Mistress adminMistress = new Mistress("Admin", LocalDate.now());
                mistressRepository.save(adminMistress);

                AppUser admin = new AppUser(
                        "admin",
                        passwordEncoder.encode("admin123"),
                        Role.ADMIN,
                        adminMistress
                );
                appUserRepository.save(admin);

                System.out.println("Дефолтный админ создан admin / admin123");
            }
        };
    }
}
