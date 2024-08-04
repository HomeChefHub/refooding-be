package refooding.api.domain.chat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import refooding.api.common.domain.BaseTimeEntity;
import refooding.api.domain.member.entity.Member;

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
    private RoomMemberState state;

    public RoomMember(Member member, Room room) {
        this.member = member;
        this.room = room;
        this.state = RoomMemberState.JOIN;
    }

    public void join() {
        this.state = RoomMemberState.JOIN;
    }

    public boolean isJoin() {
        return this.state == RoomMemberState.JOIN;
    }

    public void exit() {
        this.state = RoomMemberState.EXIT;
    }

}
