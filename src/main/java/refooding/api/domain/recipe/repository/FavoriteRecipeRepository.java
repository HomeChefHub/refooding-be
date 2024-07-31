package refooding.api.domain.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import refooding.api.domain.recipe.entity.FavoriteRecipe;

import java.util.Optional;

public interface FavoriteRecipeRepository extends JpaRepository<FavoriteRecipe, Long> {
    Optional<FavoriteRecipe> findByMemberIdAndRecipeId(Long memberId, Long recipeId);
}
