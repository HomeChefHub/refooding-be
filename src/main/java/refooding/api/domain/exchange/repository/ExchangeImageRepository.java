package refooding.api.domain.exchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import refooding.api.domain.exchange.entity.ExchangeImage;

import java.util.List;

public interface ExchangeImageRepository extends JpaRepository<ExchangeImage, Long>, ExchangeImageCustomRepository {

    @Query("select i from ExchangeImage i " +
            "where i.exchange.id = :exchangeId " +
            "and i.deletedAt is null")
    List<ExchangeImage> findAllByExchangeId(Long exchangeId);
}
