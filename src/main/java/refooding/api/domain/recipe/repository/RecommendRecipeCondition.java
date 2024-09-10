package refooding.api.domain.recipe.repository;

import java.time.LocalDateTime;

public record RecommendRecipeCondition(
        Long memberId,
        String ingredientName,
        Long lastRecipeId,
        LocalDateTime expirationDate
) {
}
