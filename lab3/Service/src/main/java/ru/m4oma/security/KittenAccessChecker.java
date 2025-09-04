package ru.m4oma.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.m4oma.models.AppUser;
import ru.m4oma.models.Role;
import ru.m4oma.repositories.KittenRepository;

@Slf4j
@Component("kittenAccessChecker")
@RequiredArgsConstructor
public class KittenAccessChecker {

    private final KittenRepository kittenRepository;

    public boolean isOwnedByCurrentMistress(int kittenId, Authentication authentication) {
        MyUser userDetails = (MyUser) authentication.getPrincipal();
        AppUser user = userDetails.getUser();
        log.info("Проверка доступа к котёнку: {}, пользователь: {}", kittenId, user.getUsername());

        if (user.getMistress() == null) return false;

        log.info("Проверка доступа к котёнку: {}, пользователь: {}", kittenId, user.getUsername());

        if (user.getRole() == Role.ADMIN) {
            return kittenRepository.findById(kittenId).isPresent();
        }
        return kittenRepository.findById(kittenId)
                .map(k -> k.getMistress().equals(user.getMistress()))
                .orElse(false);
    }
}
