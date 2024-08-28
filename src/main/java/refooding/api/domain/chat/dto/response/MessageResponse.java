package refooding.api.domain.chat.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import refooding.api.domain.chat.entity.Message;

import java.time.LocalDateTime;

public record MessageResponse(
        @Schema(description = "채팅방 아이디")
        Long roomId,

        @Schema(description = "메시지 아이디")
        Long messageId,

        @Schema(description = "메시지 전송 회원 아이디")
        Long senderId,

        @Schema(description = "메시지 전송 회원 닉네임")
        String senderName,

        @Schema(description = "메시지 내용")
        String content,

        @Schema(description = "메시지 전송 날짜")
        LocalDateTime sendTime
) {

    public static MessageResponse from(Message message) {
        return new MessageResponse(
                message.getRoom().getId(),
                message.getId(),
                message.getSender().getId(),
                message.getSender().getName(),
                message.getContent(),
                message.getCreatedAt()
        );
    }
}
