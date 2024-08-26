package refooding.api.domain.recipe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ManualResponse (
    @Schema(description = "레시피 순서")
    int seq,
    @Schema(description = "내용")
    String content
){}
