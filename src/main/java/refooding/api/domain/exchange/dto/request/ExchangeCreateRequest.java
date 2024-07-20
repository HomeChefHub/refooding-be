package refooding.api.domain.exchange.dto.request;

import refooding.api.domain.exchange.entity.Exchange;

public record ExchangeCreateRequest(
        String title,
        String content,
        long regionId
) {
    public Exchange toExchange() {
        return new Exchange(title, content, null);
    }
}
