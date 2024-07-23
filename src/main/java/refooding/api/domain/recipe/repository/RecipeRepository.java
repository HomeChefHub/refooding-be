package refooding.api.domain.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import refooding.api.domain.recipe.entity.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("select r from Recipe r " +
            "left join fetch r.manualList " +
            "left join fetch r.recipeIngredientList ri " +
            "left join fetch ri.ingredient where r.id = :id")
    Optional<Recipe> findByIdWithDetails(Long id);

    List<Recipe> findByNameContaining(String recipeName);

    List<Recipe> findByMainIngredientName(String ingredientName);

    @Query("select r from Recipe r where r.mainIngredientName in :ingredientNames")
    List<Recipe> findByMainIngredientNames(List<String> ingredientNames);
}
