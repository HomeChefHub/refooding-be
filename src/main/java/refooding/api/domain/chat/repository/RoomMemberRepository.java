package refooding.api.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import refooding.api.domain.chat.entity.RoomMember;

import java.util.List;
import java.util.Optional;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {

    @Query("select roomMember " +
            "from RoomMember roomMember " +
            "where roomMember.member.id = :memberId " +
            "and roomMember.status = 'JOIN'")
    List<RoomMember> findRoomMembersJoinedRoomsByMemberId(Long memberId);

    @Query("select roomMember " +
            "from RoomMember roomMember " +
            "left join fetch roomMember.member member " +
            "left join roomMember.room room " +
            "where roomMember.member.id in (:memberIds) " +
            "and room.exchange.id = :exchangeId " +
            "and roomMember.deletedDate is null")
    List<RoomMember> findRoomMembersByMemberIdsInAndExchangeId(List<Long> memberIds, Long exchangeId);

    List<RoomMember> findRoomMembersByRoomId(Long roomId);

    Optional<RoomMember> findJoinRoomByMemberIdAndRoomId(Long memberId, Long roomId);

    @Modifying
    @Query("update RoomMember roomMember " +
            "set roomMember.deletedDate = now()" +
            "where roomMember.id in(:roomMemberIds)")
    void deleteAllRoomMember(List<Long> roomMemberIds);
}
