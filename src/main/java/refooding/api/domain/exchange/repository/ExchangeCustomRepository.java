package refooding.api.domain.exchange.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import refooding.api.domain.exchange.dto.response.ExchangeResponse;

public interface ExchangeCustomRepository {
    Slice<ExchangeResponse> findExchangesByCondition(ExchangeSearchCondition request, Pageable pageable);
}
