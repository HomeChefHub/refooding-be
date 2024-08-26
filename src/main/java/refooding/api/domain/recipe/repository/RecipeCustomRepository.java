package refooding.api.domain.recipe.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import refooding.api.domain.recipe.dto.response.RecipeResponse;

import java.util.List;

public interface RecipeCustomRepository {
    Slice<RecipeResponse> findRecipesByCondition(String searchKeyword, Long lastRecipeId, Pageable pageable);
    Slice<RecipeResponse> findLikeRecipes(List<Long> recipeIds, Long lastLikeRecipeId, Pageable pageable);
}