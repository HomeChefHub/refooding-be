package refooding.api.domain.chat.service;

import refooding.api.domain.chat.dto.request.MessageRequest;

public interface MessageService {

    void sendMessage(MessageRequest request);

}
