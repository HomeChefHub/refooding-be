package refooding.api.domain.ingredient.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import refooding.api.domain.ingredient.entity.Ingredient;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Optional<Ingredient> findIngredientByName(String name);
}
