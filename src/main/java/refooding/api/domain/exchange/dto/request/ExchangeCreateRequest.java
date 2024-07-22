package refooding.api.domain.exchange.dto.request;

import refooding.api.domain.exchange.entity.Exchange;
import refooding.api.domain.exchange.entity.Region;

public record ExchangeCreateRequest(
        String title,
        String content,
        long regionId
) {
    public Exchange toExchange(Region region) {
        return new Exchange(title, content, region);
    }
}
