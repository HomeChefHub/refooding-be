package refooding.api.domain.ingredient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import refooding.api.domain.ingredient.entity.IngredientImage;

import java.util.List;

public interface IngredientImageRepository extends JpaRepository<IngredientImage, Long>, IngredientImageCustomRepository {
    @Query("select i from IngredientImage i " +
            "where i.memberIngredient.id = :ingredientId " +
            "and i.deletedAt is null")
    List<IngredientImage> findAllByIngredientId(Long ingredientId);
}
