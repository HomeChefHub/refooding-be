package refooding.api.domain.chat.dto.response;

import refooding.api.domain.chat.entity.Message;

import java.time.LocalDateTime;

public record MessageResponse(
        Long roomId,
        Long messageId,
        Long senderId,
        String senderName,
        String content,
        LocalDateTime sendTime
) {

    public static MessageResponse from(Message message) {
        return new MessageResponse(
                message.getRoom().getId(),
                message.getId(),
                message.getSender().getId(),
                message.getSender().getName(),
                message.getContent(),
                message.getCreatedDate()
        );
    }
}
