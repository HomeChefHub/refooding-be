package refooding.api.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import refooding.api.common.exception.CustomException;
import refooding.api.common.exception.ExceptionCode;
import refooding.api.domain.chat.dto.request.RoomCreateRequest;
import refooding.api.domain.chat.entity.JoinRoom;
import refooding.api.domain.chat.entity.Room;
import refooding.api.domain.chat.repository.JoinRoomRepository;
import refooding.api.domain.chat.repository.RoomRepository;
import refooding.api.domain.exchange.entity.Exchange;
import refooding.api.domain.exchange.repository.ExchangeRepository;
import refooding.api.domain.member.entity.Member;
import refooding.api.domain.member.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final JoinRoomRepository joinRoomRepository;
    private final RoomRepository roomRepository;
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
        Member inviter = memberRepository.findById(inviterId)
                // TODO : 에러처리
                .orElseThrow(() -> new CustomException(ExceptionCode.INTERNAL_SERVER_ERROR));
        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_MEMBER));

        List<Long> memberIds = new ArrayList<>();
        memberIds.add(inviterId);
        memberIds.add(receiver.getId());
        return findExistingRoomIdByMemberIdsAndExchangeId(memberIds, exchangeId)
                .orElseGet(() -> createRoom(inviter, receiver, exchangeId));
    }

    /**
     * 채팅을 요청하는 회원과 채팅을 초대받는 회원의 기존 채팅방이 있는지 확인
     */
    private Optional<Long> findExistingRoomIdByMemberIdsAndExchangeId(List<Long> ids, Long exchangeId){
        Map<Long, List<JoinRoom>> joinRoomsMap = joinRoomRepository.findAllByMemberIdsInAndExchangeId(ids, exchangeId)
                .stream()
                .collect(Collectors.groupingBy(joinRoom -> joinRoom.getRoom().getId()));

        for (Long roomId : joinRoomsMap.keySet()) {
            List<JoinRoom> joinRooms = joinRoomsMap.get(roomId);
            if (joinRooms.size() == ids.size()) {
                return Optional.of(roomId);
            }
        }
        return Optional.empty();
    }

    /**
     * 채팅방 생성
     */
    private Long createRoom(Member inviter, Member receiver, Long exchangeId) {
        Exchange exchange = exchangeRepository.findById(exchangeId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_EXCHANGE));
        Room room = new Room(exchange);
        roomRepository.save(room);
        joinRoomRepository.save(new JoinRoom(inviter, room));
        joinRoomRepository.save(new JoinRoom(receiver, room));
        return room.getId();
    }

}