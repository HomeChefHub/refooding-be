package refooding.api.domain.exchange.service;

import refooding.api.domain.exchange.dto.request.ExchangeCreateRequest;

public interface ExchangeService {

    Long create(ExchangeCreateRequest request);
}
