package refooding.api.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import refooding.api.domain.chat.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
