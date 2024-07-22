package refooding.api.domain.exchange.dto.response;

import refooding.api.domain.exchange.entity.ExchangeStatus;

import java.time.LocalDateTime;

public record ExchangeResponse(
        Long id,
        String title,
        String content,
        String region,
        String childRegion,
        ExchangeStatus status,
        LocalDateTime createDate
) {}
