package refooding.api.domain.exchange.entity;

import jakarta.persistence.*;
import refooding.api.domain.common.BaseTimeEntity;

@Entity
public class Exchange extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private ExchangeStatus status;

    // @ManyToOne
    // @JoinColumn(name = "id")
    // private Member member;
}
