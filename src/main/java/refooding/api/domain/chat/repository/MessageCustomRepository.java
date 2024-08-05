package refooding.api.domain.chat.repository;

import refooding.api.domain.chat.entity.Message;

import java.util.List;

public interface MessageCustomRepository {
    List<Message> findLatestMessageWithSenderByRoomId(List<Long> roomIds);
}
