package refooding.api.domain.chat.service;

import refooding.api.domain.chat.dto.request.RoomCreateRequest;

public interface RoomService {
    Long getOrCreate(RoomCreateRequest request);
}
