package refooding.api.domain.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record RoomCreateRequest(
        @NotNull(message = "채팅방 초대 대상 회원의 아이디는 null일 수 없습니다")
        @Schema(description = "채팅방 초대 대상 회원 아이디")
        Long receiverId,

        @NotNull(message = "식재료 교환글 아이디는 null일 수 없습니다")
        @Schema(description = "식재료 교환글 아이디")
        Long exchangeId
) {
}
