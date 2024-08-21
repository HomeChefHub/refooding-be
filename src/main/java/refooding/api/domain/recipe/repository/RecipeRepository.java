package refooding.api.domain.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import refooding.api.domain.recipe.entity.Recipe;

import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long>, RecipeCustomRepository {
    @Query("select r from Recipe r " +
            "left join fetch r.manuals " +
            "where r.id = :id")
    Optional<Recipe> findRecipeById(Long id);
}
