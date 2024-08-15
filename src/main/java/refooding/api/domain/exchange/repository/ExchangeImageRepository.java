package refooding.api.domain.exchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import refooding.api.domain.exchange.entity.ExchangeImage;

public interface ExchangeImageRepository extends JpaRepository<ExchangeImage, Long>, ExchangeImageCustomRepository {
}
