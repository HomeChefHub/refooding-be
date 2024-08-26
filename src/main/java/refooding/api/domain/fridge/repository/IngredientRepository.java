package refooding.api.domain.fridge.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import refooding.api.domain.fridge.entity.Ingredient;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Optional<Ingredient> findIngredientByName(String name);
}
