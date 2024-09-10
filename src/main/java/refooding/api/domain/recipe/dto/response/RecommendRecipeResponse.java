package refooding.api.domain.recipe.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record RecommendRecipeResponse(
    @Schema(description = "레시피 아이디")
    Long id,
    @Schema(description = "레시피 이름")
    String name,
    @Schema(description = "레시피 대표 이미지")
    String thumbnail,
    @Schema(description = "레시피에 사용된 재료의 만료 일자")
    LocalDateTime expirationDate
){
    @QueryProjection
    public RecommendRecipeResponse {
    }
}
