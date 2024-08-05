package refooding.api.domain.chat.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import refooding.api.domain.chat.dto.request.MessageRequest;
import refooding.api.domain.chat.service.MessageService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @MessageMapping("/chat/messages")
    public void sendMessage(@Valid @RequestBody MessageRequest request) {
        messageService.sendMessage(request);
    }

}
