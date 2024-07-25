package refooding.api.domain.exchange.repository;

import refooding.api.domain.exchange.entity.ExchangeStatus;

public record ExchangeSearchCondition(
        String keyword,
        ExchangeStatus status,
        Long regionId,
        Long lastExchangeId
) {
}
