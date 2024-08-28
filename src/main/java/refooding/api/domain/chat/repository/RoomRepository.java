package refooding.api.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import refooding.api.domain.chat.entity.Room;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long>{
    @Query("select room " +
            "from Room room " +
            "where room.id = :roomId " +
            "and room.deletedAt is null")
    Optional<Room> findRoomById(Long roomId);
}
