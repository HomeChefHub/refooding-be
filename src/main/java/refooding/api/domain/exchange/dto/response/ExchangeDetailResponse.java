package refooding.api.domain.exchange.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import refooding.api.domain.exchange.entity.Exchange;
import refooding.api.domain.exchange.entity.ExchangeImage;
import refooding.api.domain.exchange.entity.ExchangeStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ExchangeDetailResponse(
        @Schema(description = "식재료 교환 아이디")
        Long id,

        @Schema(description = "제목")
        String title,

        @Schema(description = "내용")
        String content,

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
        LocalDateTime createDate,

        @Schema(description = "이미지 목록")
        List<String> imageUrls

) {
    public static ExchangeDetailResponse from(Exchange exchange, List<String> imageUrls) {
        return new ExchangeDetailResponse(
                exchange.getId(),
                exchange.getTitle(),
                exchange.getContent(),
                exchange.getMember().getId(),
                exchange.getMember().getName(),
                exchange.getRegion().getParent().getName(),
                exchange.getRegion().getName(),
                exchange.getStatus(),
                exchange.getCreatedDate(),
                imageUrls
                );
    }
}
