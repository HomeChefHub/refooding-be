package refooding.api.domain.exchange.service;

import refooding.api.domain.exchange.dto.request.ExchangeCreateRequest;
import refooding.api.domain.exchange.dto.request.ExchangeUpdateRequest;
import refooding.api.domain.exchange.dto.response.ExchangeDetailResponse;

public interface ExchangeService {

    Long create(ExchangeCreateRequest request);

    void update(Long exchangeId, ExchangeUpdateRequest request);

    void delete(Long exchangeId);

    ExchangeDetailResponse getExchangeById(Long exchangeId);
}
