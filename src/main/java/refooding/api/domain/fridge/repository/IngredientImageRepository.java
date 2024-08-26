package refooding.api.domain.fridge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import refooding.api.domain.fridge.entity.IngredientImage;

public interface IngredientImageRepository extends JpaRepository<IngredientImage, Long>, IngredientImageCustomRepository {
}
