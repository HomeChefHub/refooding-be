package refooding.api.domain.exchange.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import refooding.api.domain.exchange.entity.ExchangeStatus;

import java.time.LocalDateTime;

public record ExchangeResponse(
        @Schema(description = "식재료 교환글 아이디")
        Long exchangeId,

        @Schema(description = "제목")
        String title,

        @Schema(description = "작성자 아이디")
        Long memberId,

        @Schema(description = "작성자 닉네임")
        String username,

        @Schema(description = "상위 지역 이름")
        String region,

        @Schema(description = "하위 지역 이름")
        String childRegion,

        @Schema(description = "교환 상태")
        ExchangeStatus status,

        @Schema(description = "생성 시간")
        LocalDateTime createDate
        // TODO : 이미지
        // TODO : 회원
) {
    @QueryProjection
    public ExchangeResponse {
    }
}
