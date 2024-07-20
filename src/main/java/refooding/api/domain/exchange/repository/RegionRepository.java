package refooding.api.domain.exchange.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import refooding.api.domain.exchange.entity.Region;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {

    @EntityGraph(attributePaths = {"children"})
    List<Region> findByParentIsNull();

}
