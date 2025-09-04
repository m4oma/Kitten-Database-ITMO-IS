package ru.m4oma.communication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import ru.m4oma.dto.KittenDto;
import ru.m4oma.dto.request.CreateKittenRequest;
import ru.m4oma.dto.request.KittensByMistressIdRequest;
import ru.m4oma.dto.request.RepaintKittenRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class KittenClient {

    private final RabbitTemplate rabbitTemplate;

    public List<KittenDto> getKittensByMistressId(int mistressId) {
        try {
            KittensByMistressIdRequest request = new KittensByMistressIdRequest(mistressId);
            Object response = rabbitTemplate.convertSendAndReceive(
                    "kitten.exchange",
                    "kitten.get.by.mistress.id",
                    request
            );

            if (response instanceof List<?> kittens &&
                    (kittens.isEmpty() || kittens.getFirst() instanceof KittenDto)) {
                return (List<KittenDto>) kittens;
            } else {
                log.warn("Unexpected response type: {}", response);
                return Collections.emptyList();
            }
        } catch (Exception e) {
            log.error("Failed to fetch kittens by mistressId {}", mistressId, e);
            return Collections.emptyList();
        }
    }

    public Optional<KittenDto> createKitten(CreateKittenRequest request) {

        KittenDto dto = new KittenDto();
        dto.setName(request.getName());
        dto.setBirthDate(request.getBirthDate());
        dto.setBreed(request.getBreed());
        dto.setColour(request.getColour());
        dto.setTailLength(request.getTailLength());
        dto.setMistressId(request.getMistressId());
        try {
            KittenDto response = (KittenDto) rabbitTemplate.convertSendAndReceive(
                    "kitten.exchange",
                    "kitten.create",
                    dto
            );
            return Optional.ofNullable(response);
        } catch (Exception e) {
            log.error("Failed to create kitten", e);
            return Optional.empty();
        }
    }

    public Optional<KittenDto> getById(int id) {
        try {
            KittenDto dto = (KittenDto) rabbitTemplate.convertSendAndReceive(
                    "kitten.exchange",
                    "kitten.get.by.id",
                    id
            );
            return Optional.ofNullable(dto);
        } catch (Exception e) {
            log.error("Failed to fetch Kitten", e);
            return Optional.empty();
        }
    }

    public Optional<KittenDto> repaintKitten(int kittenId, String newColor) {
        try {
            RepaintKittenRequest request = new RepaintKittenRequest(kittenId, newColor);
            log.info("Trying to get kitten {}", kittenId);
            KittenDto response = (KittenDto) rabbitTemplate.convertSendAndReceive(
                    "kitten.exchange",
                    "kitten.repaint",
                    request
            );
            log.info("Received from Rabbit: {}", response);
            return Optional.ofNullable(response);
        } catch (Exception e) {
            log.error("Failed to repaint kitten", e);
            return Optional.empty();
        }
    }

}

