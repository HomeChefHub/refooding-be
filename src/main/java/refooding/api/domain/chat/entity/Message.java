package refooding.api.domain.chat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import refooding.api.common.domain.BaseTimeEntity;
import refooding.api.domain.member.entity.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    public Message(String content, Member sender, Room room) {
        this.content = content;
        this.status = MessageStatus.UNREAD;
        this.sender = sender;
        this.room = room;
    }

    public boolean isUnreadByMember(RoomMember roomMember) {
        if (isSentByMember(roomMember)) {
            return false;
        }

        return isMessageNewerThanLastRead(roomMember);
    }

    private boolean isSentByMember(RoomMember roomMember) {
        Member member = roomMember.getMember();

        // 마지막 메시지가 자신이 보낸 메시지라면 알림 필요 X
        return sender.getId().equals(member.getId());
    }

    private boolean isMessageNewerThanLastRead(RoomMember roomMember) {
        LocalDateTime lastReadTime = roomMember.getLastReadTime();

        // 마지막으로 읽은 시간이 없거나, 마지막으로 읽은 시간보다 마지막 메시지의 생성 날짜가 더 클 때
        return lastReadTime == null || this.getCreatedDate().isAfter(lastReadTime);
    }

}
