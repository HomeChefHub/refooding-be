package refooding.api.domain.chat.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record RoomResponse(
        @Schema(description = "채팅방 아이디")
        Long id,

        @Schema(description = "메시지 전송 회원 아이디")
        Long senderId,

        @Schema(description = "메시지 전송 회원 닉네임")
        String senderName,

        @Schema(description = "마지막 메시지 내용")
        String lastMessage,

        @Schema(description = "읽지 않은 메시지 여부")
        boolean isUnread

        // TODO : 프로필 이미지
) {
}
