package refooding.api.domain.recipe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ManualResponse (
    @Schema(description = "내용")
    String content
){}
