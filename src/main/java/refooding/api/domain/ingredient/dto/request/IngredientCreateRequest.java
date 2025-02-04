package refooding.api.domain.ingredient.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;
import refooding.api.domain.ingredient.entity.MemberIngredient;
import refooding.api.domain.ingredient.entity.Ingredient;
import refooding.api.domain.ingredient.entity.IngredientImage;
import refooding.api.domain.member.entity.Member;

import java.time.LocalDateTime;
import java.util.List;

public record IngredientCreateRequest(
        @NotBlank(message = "재료 이름은 빈 문자열 또는 null일 수 없습니다")
        @Size(max = 30, message = "제목은 1글자 이상 30글자 이하여야 합니다")
        @Schema(description = "제목")
        String name,

        @Schema(description = "재료 이미지 파일")
        MultipartFile image,

        @NotNull(message = "재료 유효기간은 null일 수 없습니다")
        @Future(message = "유효기간은 현재 날짜 이후로 설정해야 합니다")
        LocalDateTime expirationDate
) {
        public MemberIngredient toFridgeIngredient(Member member, Ingredient ingredient, LocalDateTime expirationDate, List<IngredientImage> images) {
                return new MemberIngredient(member, ingredient, expirationDate, images);
        }
}
