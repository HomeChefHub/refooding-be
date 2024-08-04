package refooding.api.domain.chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import refooding.api.domain.chat.entity.Message;
import refooding.api.domain.chat.entity.Room;
import refooding.api.domain.member.entity.Member;

public record MessageRequest(
        @NotNull(message = "채팅방 아이디는 null일 수 없습니다")
        Long roomId,

        @NotNull(message = "메시지를 전송하는 회원의 아이디는 null일 수 없습니다")
        Long senderId,

        @NotBlank(message = "메시지를 입력하세요")
        String content
) {

        public Message toMessage(Member member, Room room) {
                return new Message(content, member, room);
        }
}
