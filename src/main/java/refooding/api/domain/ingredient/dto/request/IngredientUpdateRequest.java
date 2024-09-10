package refooding.api.domain.ingredient.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public record IngredientUpdateRequest(
        @NotBlank(message = "재료 이름은 빈 문자열 또는 null일 수 없습니다")
        @Size(max = 30, message = "제목은 1글자 이상 30글자 이하여야 합니다")
        @Schema(description = "제목")
        String name,

        @NotNull(message = "재료 유효기간은 null일 수 없습니다")
        @Future(message = "유효기간은 현재 날짜 이후로 설정해야 합니다")
        LocalDateTime expirationDate,

        @Schema(description = "이미지 목록")
        String thumbnailUrl,

        @Schema(description = "식재료 이미지 파일")
        MultipartFile image
) {
}
