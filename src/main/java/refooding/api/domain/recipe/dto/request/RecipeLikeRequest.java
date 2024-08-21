package refooding.api.domain.recipe.dto.request;

import jakarta.validation.constraints.NotNull;

public record RecipeLikeRequest(
        @NotNull(message = "레시피 아이디는 null일 수 없습니다")
        Long recipeId
) {
}
