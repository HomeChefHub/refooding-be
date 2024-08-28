package refooding.api.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import refooding.api.common.exception.CustomException;
import refooding.api.common.exception.ExceptionCode;
import refooding.api.domain.chat.dto.request.RoomCreateRequest;
import refooding.api.domain.chat.dto.response.RoomResponse;
import refooding.api.domain.chat.entity.Room;
import refooding.api.domain.chat.entity.RoomMember;
import refooding.api.domain.chat.entity.RoomMemberStatus;
import refooding.api.domain.chat.repository.MessageRepository;
import refooding.api.domain.chat.repository.RoomMemberRepository;
import refooding.api.domain.chat.repository.RoomRepository;
import refooding.api.domain.exchange.entity.Exchange;
import refooding.api.domain.exchange.repository.ExchangeRepository;
import refooding.api.domain.member.entity.Member;
import refooding.api.domain.member.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomMemberRepository roomMemberRepository;
    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;
    private final ExchangeRepository exchangeRepository;

    @Override
    @Transactional
    public Long getOrCreate(RoomCreateRequest request) {
        // TODO : 회원 도메인 구현시 적용
        // 임시 회원 아이디
        Long inviterId = 1L;
        Long receiverId = request.receiverId();
        Long exchangeId = request.exchangeId();

        // 초대자 자신에게 채팅 요청시 에외 발생
        if (inviterId.equals(receiverId)) {
            // TODO : 에러처리
            throw new CustomException(ExceptionCode.INTERNAL_SERVER_ERROR);
        }

        // 채팅에 다른 회원을 초대한 회원(inviter)과 채팅에 초대된 회원(receiver) 조회
        Member inviter = memberRepository.findByIdAndDeletedAtIsNull(inviterId)
                // TODO : 에러처리
                .orElseThrow(() -> new CustomException(ExceptionCode.INTERNAL_SERVER_ERROR));
        Member receiver = memberRepository.findByIdAndDeletedAtIsNull(receiverId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_MEMBER));

        List<Long> ids = new ArrayList<>();
        ids.add(inviter.getId());
        ids.add(receiver.getId());

        // 채팅참여 상태 Join으로 변경
        List<RoomMember> roomMembers = findExistingRoomMembers(ids, exchangeId);
        if (roomMembers.isEmpty()) {
            return createRoom(inviter, receiver, exchangeId);
        }

        roomMembers.forEach(RoomMember::join);
        return roomMembers.get(0).getId();

    }

    @Override
    public List<RoomResponse> getJoinRoomsByMemberId() {
        // TODO : 회원 도메인 구현시 적용
        // 임시 회원 아이디
        Long memberId = 1L;
        Member findMember = memberRepository.findByIdAndDeletedAtIsNull(memberId)
                // TODO : 에러처리
                .orElseThrow(() -> new CustomException(ExceptionCode.INTERNAL_SERVER_ERROR));

        // 참여중인 채팅방 조회
        List<RoomMember> findRoomMembers = roomMemberRepository.findJoinedRoomsByMemberId(findMember.getId());
        Map<Long, RoomMember> roomMemberMap = findRoomMembers.stream()
                .collect(Collectors.toMap(
                        roomMember -> roomMember.getRoom().getId(),
                        roomMember -> roomMember
                ));
        List<Long> joinedRoomIds = new ArrayList<>(roomMemberMap.keySet());

        // 참여중인 채팅방의 마지막 메시지 조회
        return messageRepository.findLatestMessagesByRoomIds(joinedRoomIds)
                .stream()
                .map(message -> {
                    RoomMember roomMember = roomMemberMap.get(message.getRoom().getId());
                    return new RoomResponse(
                            message.getRoom().getId(),
                            message.getSender().getId(),
                            message.getSender().getName(),
                            message.getContent(),
                            message.isUnreadByMember(roomMember)
                    );
                })
                .toList();
    }

    @Override
    @Transactional
    public void exitRoom(Long roomId) {

        // TODO : 회원 도메인 구현시 적용
        // 임시 회원 아이디
        Long memberId = 1L;
        Member findMember = memberRepository.findByIdAndDeletedAtIsNull(memberId)
                // TODO : 에러처리
                .orElseThrow(() -> new CustomException(ExceptionCode.INTERNAL_SERVER_ERROR));

        // 채팅방 조회 및 나가기
        List<RoomMember> findRoomMembers = roomMemberRepository.findRoomMembersByRoomId(roomId);
        RoomMember exitingMember = findRoomMembers.stream()
                .filter(roomMember -> findMember.getId().equals(roomMember.getMember().getId()))
                .findFirst()
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_CHAT_ROOM));
        exitingMember.exit();

        // 해당 채팅방을 모든 회원이 나갔다면 채팅방, 채팅참여 삭제
        if (isRoomEmpty(findRoomMembers)) {
            deleteRoom(exitingMember.getRoom(), findRoomMembers);
        }

    }

    private static boolean isRoomEmpty(List<RoomMember> findRoomMembers) {
        List<RoomMember> joinedRoomMembers = findRoomMembers
                .stream()
                .filter(roomMember -> roomMember.getStatus().equals(RoomMemberStatus.JOIN))
                .toList();
        return joinedRoomMembers.isEmpty();
    }

    /**
     * 채팅방, 채탕참여 삭제
     */
    private void deleteRoom(Room room, List<RoomMember> findRoomMembers) {
        room.delete();
        List<Long> roomMembers = findRoomMembers
                .stream()
                .map(RoomMember::getId)
                .toList();
        roomMemberRepository.deleteAllRoomMember(roomMembers);
    }

    /**
     * 채팅을 요청하는 회원과 채팅을 초대받는 회원의 기존 채팅방이 있는지 확인
     */
    private List<RoomMember> findExistingRoomMembers(List<Long> ids, Long exchangeId){
        return roomMemberRepository.findRoomMembersByMemberIdsAndExchangeId(ids, exchangeId);
    }

    /**
     * 채팅방 생성
     */
    private Long createRoom(Member inviter, Member receiver, Long exchangeId) {
        Exchange exchange = exchangeRepository.findExchangeById(exchangeId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_EXCHANGE));
        Room room = new Room(exchange);
        roomRepository.save(room);
        roomMemberRepository.save(new RoomMember(inviter, room));
        roomMemberRepository.save(new RoomMember(receiver, room));
        return room.getId();
    }

}
