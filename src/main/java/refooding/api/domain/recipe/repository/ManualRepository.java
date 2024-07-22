package refooding.api.domain.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import refooding.api.domain.recipe.entity.Manual;

public interface ManualRepository extends JpaRepository<Manual, Long> {
}
