package refooding.api.domain.exchange.service;

import refooding.api.domain.exchange.dto.request.ExchangeCreateRequest;
import refooding.api.domain.exchange.dto.request.ExchangeUpdateRequest;

public interface ExchangeService {

    Long create(ExchangeCreateRequest request);

    void update(Long exchangeId, ExchangeUpdateRequest request);

    void delete(Long exchangeId);
}
