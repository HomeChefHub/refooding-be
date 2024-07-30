package refooding.api.domain.exchange.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import refooding.api.domain.exchange.entity.ExchangeStatus;

public record ExchangeUpdateRequest(
        @NotBlank(message = "제목은 빈 문자열 또는 null일 수 없습니다")
        @Size(max = 30, message = "제목은 1글자 이상 30글자 이하여야 합니다")
        String title,

        @NotBlank(message = "내용은 빈 문자열 또는 null일 수 없습니다")
        @Size(max = 30, message = "제목은 1글자 이상 130글자 이하여야 합니다")
        String content,

        @NotNull(message = "지역을 입력해주세요")
        Long regionId,

        @NotNull(message = "교환 상태를 입력해주세요")
        ExchangeStatus status
) {}
