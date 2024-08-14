package refooding.api.domain.exchange.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import refooding.api.common.domain.BaseTimeEntity;
import refooding.api.domain.member.entity.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Exchange extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ExchangeStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    // TODO : 회원 추가
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "exchange")
    private List<ExchangeImage> images = new ArrayList<>();

    private String thumbnailUrl;

    public Exchange(String title, String content, Region region, Member member, List<ExchangeImage> images) {
        this.title = title;
        this.content = content;
        this.status = ExchangeStatus.ACTIVE;
        this.region = region;
        this.member = member;
        this.images.addAll(images);
        this.thumbnailUrl = !images.isEmpty() ? images.get(0).getUrl() : null;
    }

    public void updateExchange(String title, String content, ExchangeStatus status, Region region) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.region = region;
    }

    public void delete(){
        this.deletedDate = LocalDateTime.now();
    }

    public boolean validateMember(Long memberId) {
        return getMember().getId().equals(memberId);
    }
}
