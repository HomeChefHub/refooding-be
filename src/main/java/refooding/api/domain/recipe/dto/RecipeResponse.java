package refooding.api.domain.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RecipeResponse {

    private Long recipeId;
    private String recipeName;
    private String recipeImgSrc;
}
