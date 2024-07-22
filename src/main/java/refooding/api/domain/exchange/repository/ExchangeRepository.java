package refooding.api.domain.exchange.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import refooding.api.domain.exchange.entity.Exchange;

import java.util.Optional;

public interface ExchangeRepository extends JpaRepository<Exchange, Long> {

    @EntityGraph(attributePaths = {"region"})
    Optional<Exchange> findWithRegionById(Long id);
}
