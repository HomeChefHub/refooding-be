package refooding.api.domain.recipe.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class RecipeData {
    @JsonProperty("recipes")
    private List<Recipe> recipes;

    @Getter
    public static class Recipe {
        @JsonProperty("RECIPE_NAME")
        private String name;
        @JsonProperty("INGREDIENTS")
        private String ingredientsDetails;
        @JsonProperty("HASH_TAG")
        private String mainIngredientName;
        @JsonProperty("TIP")
        private String tip;
        @JsonProperty("THUMBNAIL")
        private String mainImage;
        @JsonProperty("MANUALS")
        private List<Manual> manuals; // 메뉴얼 개수는 레시피마다 달라질 수 있으므로 리스트로 저장

    }

    @Getter
    public static class Manual {
        @JsonProperty("SEQ")
        private int seq;
        @JsonProperty("CONTENT")
        private String content;
        @JsonProperty("IMAGE_URL")
        private String imageUrl;

    }

}
