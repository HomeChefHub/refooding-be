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
public class RoomMember extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomMemberStatus status;

    private LocalDateTime joinedTime;

    private LocalDateTime lastReadTime;

    public RoomMember(Member member, Room room) {
        this.member = member;
        this.room = room;
        this.status = RoomMemberStatus.JOIN;
    }

    public void join() {
        this.status = RoomMemberStatus.JOIN;
    }

    public boolean isJoin() {
        return this.status == RoomMemberStatus.JOIN;
    }

    public void exit() {
        this.status = RoomMemberStatus.EXIT;
    }

    public void delete() {
        this.deletedDate = LocalDateTime.now();
    }

    public void updateLastReadTime() {
        this.lastReadTime = LocalDateTime.now();
    }

}
