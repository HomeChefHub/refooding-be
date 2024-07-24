package refooding.api.domain.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import refooding.api.domain.recipe.entity.Ingredient;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Optional<Ingredient> findByName(String name);
}
