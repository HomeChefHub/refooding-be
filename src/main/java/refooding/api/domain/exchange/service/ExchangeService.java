package refooding.api.domain.exchange.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import refooding.api.domain.exchange.dto.request.ExchangeCreateRequest;
import refooding.api.domain.exchange.dto.request.ExchangeUpdateRequest;
import refooding.api.domain.exchange.dto.response.ExchangeDetailResponse;
import refooding.api.domain.exchange.dto.response.ExchangeResponse;
import refooding.api.domain.exchange.entity.ExchangeStatus;

public interface ExchangeService {

    Slice<ExchangeResponse> findExchanges(String keyword, ExchangeStatus status, Long regionId, Long lastExchangeId, Pageable pageable);

    Long create(ExchangeCreateRequest request);

    void update(Long exchangeId, ExchangeUpdateRequest request);

    void delete(Long exchangeId);

    ExchangeDetailResponse getExchangeById(Long exchangeId);
}
