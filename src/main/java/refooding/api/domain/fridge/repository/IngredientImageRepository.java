package refooding.api.domain.fridge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import refooding.api.domain.fridge.entity.IngredientImage;

import java.util.List;

public interface IngredientImageRepository extends JpaRepository<IngredientImage, Long>, IngredientImageCustomRepository {
    @Query("select i from IngredientImage i " +
            "where i.fridgeIngredient.id = :ingredientId " +
            "and i.deletedDate is null")
    List<IngredientImage> findAllByIngredientId(Long ingredientId);
}
