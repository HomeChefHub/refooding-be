package refooding.api.domain.recipe.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;

public record RecipeResponse (
    @Schema(description = "레시피 아이디")
    Long id,
    @Schema(description = "레시피 이름")
    String name,
    @Schema(description = "레시피 대표 이미지")
    String thumbnail
){
    @QueryProjection
    public RecipeResponse {
    }
}
