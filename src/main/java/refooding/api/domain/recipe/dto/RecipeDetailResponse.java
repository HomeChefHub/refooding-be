package refooding.api.domain.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class RecipeDetailResponse {
    private String name;
    private String imgSrc;
    private String tip;
    private List<ManualResponse> manualResponseList;
    private List<RecipeIngredientResponse> recipeIngredientResponseList;

}
