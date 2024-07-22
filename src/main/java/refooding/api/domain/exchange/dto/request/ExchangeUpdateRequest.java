package refooding.api.domain.exchange.dto.request;

import refooding.api.domain.exchange.entity.ExchangeStatus;

public record ExchangeUpdateRequest(
        String title,
        String content,
        long regionId,
        ExchangeStatus status
) {}
