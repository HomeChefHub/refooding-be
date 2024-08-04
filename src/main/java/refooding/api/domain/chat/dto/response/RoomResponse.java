package refooding.api.domain.chat.dto.response;

import com.querydsl.core.annotations.QueryProjection;

public record RoomResponse(
        Long id,
        Long senderId,
        String senderName,
        String lastMessage
        // TODO : 프로필 이미지
) {
    @QueryProjection
    public RoomResponse {
    }
}
