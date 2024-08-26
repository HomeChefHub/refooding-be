package refooding.api.domain.fridge.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record IngredientResponse(
        @Schema(description = "재료 아이디")
        Long id,
        @Schema(description = "재료명")
        String name,
        @Schema(description = "재료 만료까지 남은 일수")
        Integer daysUntilExpiration,
        @Schema(description = "재료 등록날짜")
        LocalDateTime storageStartDate,
        @Schema(description = "재료 만료날짜")
        LocalDateTime expirationDate,
        @Schema(description = "재료 대표 이미지")
        String thumbnailUrl
) {
    @QueryProjection
    public IngredientResponse{}

}
