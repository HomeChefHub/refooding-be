package refooding.api.domain.chat.dto.request;

public record RoomCreateRequest(
        Long receiverId,
        Long exchangeId
) {
}
