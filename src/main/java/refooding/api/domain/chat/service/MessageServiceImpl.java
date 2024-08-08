package refooding.api.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import refooding.api.common.exception.CustomException;
import refooding.api.common.exception.ExceptionCode;
import refooding.api.domain.chat.dto.request.MessageRequest;
import refooding.api.domain.chat.dto.response.MessageResponse;
import refooding.api.domain.chat.entity.Message;
import refooding.api.domain.chat.entity.Room;
import refooding.api.domain.chat.repository.RoomMemberRepository;
import refooding.api.domain.chat.repository.MessageRepository;
import refooding.api.domain.chat.repository.RoomRepository;
import refooding.api.domain.member.entity.Member;
import refooding.api.domain.member.repository.MemberRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{

    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public void sendMessage(MessageRequest request) {

        Member findSender = memberRepository.findByIdAndDeletedDateIsNull(request.senderId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_MEMBER));
        Room findRoom = roomRepository.findRoomById(request.roomId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_CHAT_ROOM));

        // 메시지 발신 회원이 해당 채팅방에 참여중인 회원인지 확인
        roomMemberRepository.findJoinRoomByMemberIdAndRoomId(findSender.getId(), findRoom.getId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_CHAT_ROOM));

        Message message = request.toMessage(findSender, findRoom);
        Message savedMessage = messageRepository.save(message);

        MessageResponse response = MessageResponse.from(savedMessage);
        messagingTemplate.convertAndSend(
                String.format("/sub/chat/rooms/%s", findRoom.getId()),
                response
        );

    }
}
