package ru.m4oma.communication;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import ru.m4oma.dto.MistressDto;
import ru.m4oma.dto.MistressGetAllRequestDto;
import ru.m4oma.dto.PageWrapper;
import ru.m4oma.service.MistressService;

@Slf4j
@RequiredArgsConstructor
@Component
public class MistressListener {

    private final MistressService mistressService;

    @RabbitListener(queues = "mistress.get.by.id", containerFactory = "rabbitListenerContainerFactory")
    public MistressDto handleGetById(int id) {
        return mistressService.getById(id);
    }


    @RabbitListener(queues = "mistress.get.all")
    public PageWrapper<MistressDto> handleGetAll(MistressGetAllRequestDto request) {
        Page<MistressDto> page = mistressService.findAll(request.getFilter(),
                PageRequest.of(request.getPage(), request.getSize()));

        return new PageWrapper<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @RabbitListener(queues = "mistress.create")
    public MistressDto handleCreate(MistressDto dto) {
        return mistressService.create(dto.getName(), dto.getBirthDate());
    }

    @RabbitListener(queues = "mistress.delete")
    public void handleDelete(Integer id) {
        MistressDto dto = mistressService.getById(id);
        mistressService.delete(dto);
    }
}

