package refooding.api.domain.exchange.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import refooding.api.domain.exchange.dto.request.ExchangeCreateRequest;
import refooding.api.domain.exchange.dto.request.ExchangeUpdateRequest;
import refooding.api.domain.exchange.entity.Exchange;
import refooding.api.domain.exchange.entity.Region;
import refooding.api.domain.exchange.repository.ExchangeRepository;
import refooding.api.domain.exchange.repository.RegionRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeService{

    private final ExchangeRepository exchangeRepository;
    private final RegionRepository regionRepository;

    @Override
    @Transactional
    public Long create(ExchangeCreateRequest request) {
        // TODO : 에러처리
        Region region = regionRepository.findById(request.regionId()).orElseThrow();
        Exchange exchange = request.toExchange(region);

        exchangeRepository.save(exchange);
        return exchange.getId();
    }

    @Override
    @Transactional
    public void update(Long exchangeId, ExchangeUpdateRequest request) {
        // TODO : 에러처리
        Exchange exchange = exchangeRepository.findById(exchangeId).orElseThrow();
        Region region = regionRepository.findById(request.regionId()).orElseThrow();

        exchange.updateExchange(
                request.title(),
                request.content(),
                request.status(),
                region
        );
    }

    @Override
    @Transactional
    public void delete(Long exchangeId) {
        // TODO : 에러처리
        Exchange exchange = exchangeRepository.findById(exchangeId).orElseThrow();
        exchange.delete();
    }

}
