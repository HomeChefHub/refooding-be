package refooding.api.domain.exchange;

import jakarta.persistence.*;
import lombok.Getter;
import refooding.api.domain.common.BaseTimeEntity;

@Entity
@Getter
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
    @JoinColumn(name = "id", nullable = false)
    private Region region;

    // @ManyToOne
    // @JoinColumn(name = "id")
    // private Member member;
}
