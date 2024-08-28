package refooding.api.domain.fridge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import refooding.api.domain.fridge.entity.FridgeIngredient;

import java.util.Optional;

public interface FridgeIngredientRepository extends JpaRepository<FridgeIngredient, Long>, FridgeIngredientCustomRepository {
    @Query("select fi from FridgeIngredient fi " +
            "where fi.id = :id " +
            "and fi.deletedAt is null")
    Optional<FridgeIngredient> findFridgeIngredientById(Long id);
}
