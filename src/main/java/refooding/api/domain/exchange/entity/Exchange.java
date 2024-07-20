package refooding.api.domain.exchange.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import refooding.api.common.domain.BaseTimeEntity;

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
    // @ManyToOne
    // @JoinColumn(name = "id")
    // private Member member;

    public Exchange(String title, String content, Region region) {
        this.title = title;
        this.content = content;
        this.status = ExchangeStatus.ACTIVE;
        this.region = region;
    }

    public void updateRegion(Region region) {
        this.region = region;
    }
}
