package refooding.api.domain.exchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import refooding.api.domain.exchange.entity.Exchange;

import java.util.Optional;

public interface ExchangeRepository extends JpaRepository<Exchange, Long>, ExchangeCustomRepository {

    @Query("select e from Exchange e " +
            "join fetch e.member m " +
            "join fetch e.region r " +
            "join fetch r.parent pr " +
            "where e.id = :exchangeId " +
            "and e.deletedAt is null")
    Optional<Exchange> findExchangeById(Long exchangeId);
}
