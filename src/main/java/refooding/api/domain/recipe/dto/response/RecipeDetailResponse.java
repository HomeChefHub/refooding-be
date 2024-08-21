package refooding.api.domain.recipe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import refooding.api.domain.recipe.entity.Recipe;

import java.util.List;


public record RecipeDetailResponse (
    @Schema(description = "레시피 아이디")
    Long id,
    @Schema(description = "레시피 이름")
    String name,
    @Schema(description = "레시피 재료")
    String ingredients,
    @Schema(description = "레시피 팁")
    String tip,
    @Schema(description = "대표 이미지")
    String thumbnail,
    @Schema(description = "레시피 매뉴얼")
    List<ManualResponse> manuals
     ){
    public static RecipeDetailResponse from(Recipe recipe) {
        List<ManualResponse> manualResponses = recipe.getManuals()
                .stream()
                .map(manual -> new ManualResponse(manual.getSeq(), manual.getContent(), manual.getImageUrl()))
                .toList();
        return new RecipeDetailResponse(
                recipe.getId(),
                recipe.getName(),
                recipe.getIngredients(),
                recipe.getTip(),
                recipe.getThumbnail(),
                manualResponses
        );
    }
}
