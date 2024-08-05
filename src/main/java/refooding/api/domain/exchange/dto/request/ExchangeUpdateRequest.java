package refooding.api.domain.exchange.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import refooding.api.domain.exchange.entity.ExchangeStatus;

public record ExchangeUpdateRequest(
        @NotBlank(message = "제목은 빈 문자열 또는 null일 수 없습니다")
        @Size(max = 30, message = "제목은 1글자 이상 30글자 이하여야 합니다")
        @Schema(description = "제목")
        String title,

        @NotBlank(message = "내용은 빈 문자열 또는 null일 수 없습니다")
        @Size(max = 30, message = "제목은 1글자 이상 130글자 이하여야 합니다")
        @Schema(description = "내용")
        String content,

        @NotNull(message = "지역을 입력해주세요")
        @Schema(description = "하위 지역 아이디")
        Long regionId,

        @NotNull(message = "교환 상태를 입력해주세요")
        @Schema(description = "교환 상태")
        ExchangeStatus status
) {}
