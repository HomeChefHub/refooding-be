package refooding.api.domain.recipe.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import refooding.api.domain.recipe.entity.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("select r from Recipe r " +
            "left join fetch r.manualList " +
            "left join fetch r.recipeIngredientList ri " +
            "left join fetch ri.ingredient where r.id = :id")
    Optional<Recipe> findByIdWithDetails(Long id);

    @Query("select r from Recipe r")
    Slice<Recipe> findAllBySlice(Pageable pageable);
    Slice<Recipe> findByNameContaining(String recipeName, Pageable pageable);

    @Query("select r from Recipe r where r.mainIngredientName in :ingredientNames")
    Slice<Recipe> findByMainIngredientNames(List<String> ingredientNames, Pageable pageable);
}
