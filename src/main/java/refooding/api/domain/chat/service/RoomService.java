package refooding.api.domain.chat.service;

import refooding.api.domain.chat.dto.request.RoomCreateRequest;
import refooding.api.domain.chat.dto.response.RoomResponse;

import java.util.List;

public interface RoomService {
    Long getOrCreate(RoomCreateRequest request);

    List<RoomResponse> getJoinRoomsByMemberId();

    void exitRoom(Long roomId);
}
