package refooding.api.domain.chat.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import refooding.api.domain.chat.entity.Message;
import refooding.api.domain.chat.entity.QMessage;

import java.util.List;

import static refooding.api.domain.chat.entity.QMessage.message;
import static refooding.api.domain.member.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class MessageCustomRepositoryImpl implements MessageCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Message> findLatestMessageWithSenderByRoomId(List<Long> roomIds) {

        QMessage subMessage = new QMessage("subMessage");

        return jpaQueryFactory
                .select(message)
                .from(message)
                .leftJoin(message.sender, member)
                .fetchJoin()
                .where(message.id.in(
                        JPAExpressions
                                .select(subMessage.id.max())
                                .from(subMessage)
                                .groupBy(subMessage.room.id)
                ))
                .fetch();
    }
}
