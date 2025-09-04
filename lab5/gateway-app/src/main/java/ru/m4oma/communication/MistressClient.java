package ru.m4oma.communication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import ru.m4oma.dto.MistressDto;
import ru.m4oma.dto.MistressFilter;
import ru.m4oma.dto.request.CreateMistressRequest;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class MistressClient {

    private final RabbitTemplate rabbitTemplate;

    public Optional<MistressDto> getMistressById(int id) {
        log.info("getMistressById with id {}", id);
        try {
            MistressDto dto = (MistressDto) rabbitTemplate.convertSendAndReceive(
                    "mistress.exchange",
                    "mistress.get.by.id",
                    id
            );
            return Optional.ofNullable(dto);
        } catch (Exception e) {
            log.error("Failed to fetch Mistress by ID", e);
            return Optional.empty();
        }
    }

    public List<MistressDto> getAll(MistressFilter filter, int page, int size) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("filter", filter);
            payload.put("page", page);
            payload.put("size", size);

            Object result = rabbitTemplate.convertSendAndReceive(
                    "mistress.exchange",
                    "mistress.get.all",
                    payload
            );
            return result != null ? (List<MistressDto>) result : Collections.emptyList();
        } catch (Exception e) {
            log.error("Failed to fetch all Mistresses", e);
            return Collections.emptyList();
        }
    }

    public Optional<MistressDto> createMistress(CreateMistressRequest request) {
        MistressDto dto = new MistressDto();
        dto.setName(request.getName());
        dto.setBirthDate(request.getBirthDate());
        try {
            MistressDto result = (MistressDto) rabbitTemplate.convertSendAndReceive(
                    "mistress.exchange",
                    "mistress.create",
                    dto
            );
            return Optional.ofNullable(result);
        } catch (Exception e) {
            log.error("Failed to create Mistress", e);
            return Optional.empty();
        }
    }

    public boolean deleteMistress(int id) {
        try {
            rabbitTemplate.convertSendAndReceive(
                    "mistress.exchange",
                    "mistress.delete",
                    id
            );
            return true;
        } catch (Exception e) {
            log.error("Failed to delete Mistress", e);
            return false;
        }
    }
}
