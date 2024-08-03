package refooding.api.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import refooding.api.domain.chat.entity.JoinRoom;
import refooding.api.domain.chat.entity.Room;
import refooding.api.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface JoinRoomRepository extends JpaRepository<JoinRoom, Long>, JoinRoomCustomRepository {

    @Query("select joinroom " +
            "from JoinRoom joinroom " +
            "left join joinroom.room room " +
            "where joinroom.member.id in (:memberIds) " +
            "and room.exchange.id = :exchangeId")
    List<JoinRoom> findAllByMemberIdsInAndExchangeId(List<Long> memberIds, Long exchangeId);

}
