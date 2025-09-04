package ru.m4oma.exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.m4oma.communication.KittenClient;
import ru.m4oma.dto.KittenDto;
import ru.m4oma.model.AppUser;
import ru.m4oma.model.Role;
import ru.m4oma.security.MyUserDetails;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class KittenAccessChecker {
    private final KittenClient kittenClient;
    public boolean hasAccessToKitten(int kittenId, Authentication authentication) {
        AppUser user = ((MyUserDetails) authentication.getPrincipal()).getUser();

        Optional<KittenDto> kittenOpt = kittenClient.getById(kittenId);

        if (user.getRole() == Role.ADMIN) {
            return true;
        }
        if (kittenOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Kitten not found");
        }

        KittenDto kitten = kittenOpt.get();
        if (user.getMistressId() != kitten.getMistressId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this kitten");
        }
        return true;
    }
}

