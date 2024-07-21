package refooding.api.domain.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import refooding.api.domain.recipe.entity.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
