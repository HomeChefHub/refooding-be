package refooding.api.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import refooding.api.domain.chat.entity.RoomMember;
import refooding.api.domain.chat.entity.Room;
import refooding.api.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {

    @Query("select roomMember " +
            "from RoomMember roomMember " +
            "where roomMember.member.id = :memberId " +
            "and roomMember.state = 'JOIN'")
    List<RoomMember> findAllJoinedRoomsByMemberId(Long memberId);

    @Query("select roomMember " +
            "from RoomMember roomMember " +
            "left join roomMember.room room " +
            "where roomMember.member.id in (:memberIds) " +
            "and room.exchange.id = :exchangeId")
    List<RoomMember> findAllByMemberIdsInAndExchangeId(List<Long> memberIds, Long exchangeId);

    Optional<RoomMember> findJoinRoomByMemberAndRoom(Member member, Room room);
}
