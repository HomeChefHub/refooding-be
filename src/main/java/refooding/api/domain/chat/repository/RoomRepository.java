package refooding.api.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import refooding.api.domain.chat.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long>{


}
