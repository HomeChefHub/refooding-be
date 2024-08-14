package refooding.api.domain.exchange.repository;

import refooding.api.domain.exchange.entity.ExchangeImage;

import java.util.List;

public interface ExchangeImageCustomRepository {
    void saveAll(List<ExchangeImage> images);
}
