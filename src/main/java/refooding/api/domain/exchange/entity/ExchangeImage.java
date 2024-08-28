package refooding.api.domain.exchange.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import refooding.api.common.domain.BaseTimeEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExchangeImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exchange_id")
    private Exchange exchange;

    private String url;

    public ExchangeImage(Exchange exchange, String url) {
        this.exchange = exchange;
        this.url = url;
    }

    public ExchangeImage(String url) {
        this(null, url);
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }

    public void delete() {
        this.deletedDate = LocalDateTime.now();
    }
}
