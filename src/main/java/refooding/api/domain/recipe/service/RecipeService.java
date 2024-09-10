package refooding.api.domain.recipe.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import refooding.api.domain.recipe.dto.response.RecipeDetailResponse;
import refooding.api.domain.recipe.dto.response.RecipeLikeResponse;
import refooding.api.domain.recipe.dto.response.RecipeResponse;
import refooding.api.domain.recipe.dto.response.RecommendRecipeResponse;

import java.time.LocalDateTime;

public interface RecipeService {
    Slice<RecipeResponse> getRecipes(String keyword, Long lastRecipeId, Pageable pageable);

    Slice<RecommendRecipeResponse> getRecommendedRecipes(Long memberId, String ingredientName, Long lastRecipeId, LocalDateTime expirationDate, Pageable pageable);

    Slice<RecipeResponse> getLikeRecipes(Long memberId, Long lastRecipeId, Pageable pageable);

    RecipeDetailResponse getRecipeById(Long recipeId);

    RecipeLikeResponse toggleRecipeLike(Long memberId, Long recipeId);
}
