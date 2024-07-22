package refooding.api.domain.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import refooding.api.domain.recipe.entity.RecipeIngredient;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
}
