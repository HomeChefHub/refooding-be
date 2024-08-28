package refooding.api.domain.exchange.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import refooding.api.domain.exchange.dto.request.ExchangeCreateRequest;
import refooding.api.domain.exchange.dto.request.ExchangeUpdateRequest;
import refooding.api.domain.exchange.dto.response.ExchangeDetailResponse;
import refooding.api.domain.exchange.dto.response.ExchangeResponse;
import refooding.api.domain.exchange.entity.ExchangeStatus;

public interface ExchangeService {

    Slice<ExchangeResponse> getExchanges(String keyword, ExchangeStatus status, Long regionId, Long lastExchangeId, Pageable pageable);

    ExchangeDetailResponse getExchangeById(Long exchangeId);

    Long create(Long memberId, ExchangeCreateRequest request);

    void update(Long memberId, Long exchangeId, ExchangeUpdateRequest request);

    void delete(Long memberId, Long exchangeId);

}