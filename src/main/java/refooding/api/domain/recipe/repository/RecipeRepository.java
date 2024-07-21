package refooding.api.domain.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import refooding.api.domain.recipe.entity.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
