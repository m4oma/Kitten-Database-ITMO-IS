package ru.m4oma.communication;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.m4oma.dto.KittenDto;
import ru.m4oma.dto.KittensByMistressIdRequest;
import ru.m4oma.dto.RepaintKittenRequest;
import ru.m4oma.service.KittenService;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KittenListener {

    private final KittenService kittenService;

    @RabbitListener(queues = "kitten.create")
    public KittenDto handleCreateKitten(KittenDto dto) {
        log.info("Creating kitten: {}", dto);
        return kittenService.save(dto);
    }

    @RabbitListener(queues = "kitten.get.by.mistress.id")
    public List<KittenDto> handleGetKittensByMistressId(KittensByMistressIdRequest request) {
        return kittenService.findByMistressId(request.getMistressId());
    }

    @RabbitListener(queues = "kitten.get.by.id")
    public KittenDto handleGetById(int id) {
        log.info("Fetching kitten with id {}", id);
        return kittenService.getById(id);
    }

    @RabbitListener(queues = "kitten.repaint")
    public KittenDto handleRepaintKitten(RepaintKittenRequest request) {
        try {
            log.info("Returning repainted kitten with id={}, color={}", request.getKittenId(), request.getNewColor());
            return kittenService.repaint(request.getKittenId(), request.getNewColor());
        } catch (EntityNotFoundException e) {
            log.warn("Kitten not found: {}", request.getKittenId());

            return null;
        }
    }
}

